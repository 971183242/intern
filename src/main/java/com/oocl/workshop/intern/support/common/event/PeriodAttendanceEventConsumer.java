package com.oocl.workshop.intern.support.common.event;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.oocl.workshop.intern.app.service.EmailService;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceDomService;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomService;
import com.oocl.workshop.intern.domain.report.service.MonthlySettlementDayRuleService;
import com.oocl.workshop.intern.interfaces.dto.email.AttendanceDTO4Email;
import com.oocl.workshop.intern.interfaces.dto.email.MailSenderDTO;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Session;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.oocl.workshop.intern.support.ActiveMQConfig.REPORT_QUEUE;

@Component
public class PeriodAttendanceEventConsumer {

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${intern.system.environment}")
    private String environment;

    @Autowired
    private ProfileDomService profileDomService;

    @Autowired
    private AttendanceDomService attendanceDomService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MonthlySettlementDayRuleService monthlySettlementDayRuleService;

    private final Logger logger = LoggerFactory.getLogger(PeriodAttendanceEventConsumer.class);

    @Value("${intern.system.url}")
    private String internSystemUrl;

    @JmsListener(destination = REPORT_QUEUE)
    public void receiveReportMessage(@Payload DomainEvent event,
                                     @Headers MessageHeaders headers,
                                     Message message, Session session) throws MessagingException, IOException, TemplateException {
        List<Team> teamList = profileDomService.findAllTeams();
        MailSenderDTO mailDto = new MailSenderDTO();
        mailDto.setFrom(emailFrom);
        String emailTo = String.join(";", teamList.stream().filter(team -> team.getTeamLeader() != null).map(team -> team.getTeamLeader().getEmail()).collect(Collectors.toList()));
        mailDto.setTo(emailTo);
        mailDto.setSubject(environment.equals("prd") ? "实习生管理系统-审批报表" : "测试-实习生管理系统-审批报表");
        mailDto.setTemplateName("email-template-reporter.ftl");
        Map<String, Object> context = new HashMap<>();
        Date today = new Date();
        today.setMonth(today.getMonth() - 1);
        List<Date> timeWindow = monthlySettlementDayRuleService.getMonthlySettlementDateWindow(today);
        List<AttendanceDTO4Email> attendanceDTOList = getAttendanceDTO4Emails(timeWindow.get(0), timeWindow.get(1), teamList);
        context.put("attendance", attendanceDTOList);
        context.put("approveUrl", internSystemUrl + "leader");
        mailDto.setModel(context);
        emailService.sendEmailWithTemplate(mailDto);
        logger.info(new Gson().toJson(mailDto));
    }

    private List<AttendanceDTO4Email> getAttendanceDTO4Emails(Date dateFrom, Date dateTo, List<Team> teamList) {
        List<AttendanceDTO4Email> attendanceDTOList = Lists.newArrayList();

        teamList.forEach(team -> {
            List<Intern> internList = profileDomService.findTeamInterns(team.getTeamId(), dateFrom, dateTo);
            internList.forEach(intern -> {
                PeriodAttendance periodAttendance = attendanceDomService.getPeriodAttendance(intern.getDomainId(), dateFrom, dateTo);
                AttendanceDTO4Email attendanceDTO = new AttendanceDTO4Email();
                String internId = periodAttendance.getInternId();
                attendanceDTO.setInternName(profileDomService.findUserByDomainId(internId).get().getName());
                attendanceDTO.setApprovedDays(periodAttendance.getApprovedAttendanceCount());
                attendanceDTO.setRejectedDays(periodAttendance.getRejectedAttendanceCount());
                attendanceDTO.setCheckInDays(periodAttendance.getCheckedInAttendanceCount());
                attendanceDTO.setTeam(intern.getTeam().getName().toUpperCase());
                attendanceDTOList.add(attendanceDTO);
            });
        });
        return attendanceDTOList;
    }
}
