package com.demo.workshop.intern.support.common.event;

import com.demo.workshop.intern.app.service.EmailService;
import com.demo.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.demo.workshop.intern.domain.profile.entity.Intern;
import com.demo.workshop.intern.domain.profile.entity.Role;
import com.demo.workshop.intern.domain.profile.entity.Team;
import com.demo.workshop.intern.domain.profile.entity.User;
import com.demo.workshop.intern.domain.profile.service.ProfileDomService;
import com.demo.workshop.intern.domain.report.service.MonthlySettlementDayRuleService;
import com.demo.workshop.intern.interfaces.dto.email.AttendanceDTO4Email;
import com.demo.workshop.intern.interfaces.dto.email.MailSenderDTO;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.demo.workshop.intern.domain.attendance.service.AttendanceDomService;
import com.oocl.workshop.intern.domain.profile.entity.*;
import freemarker.template.TemplateException;
import org.apache.commons.lang.time.DateUtils;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.demo.workshop.intern.support.ActiveMQConfig.REPORT_QUEUE;

@Component
public class PeriodAttendanceEventConsumer {

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${spring.mail.properties.mail.subject}")
    private String emailSubject;

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
        mailDto.setTo(getEmailTo());
        mailDto.setCc(getAdminEmail());
        mailDto.setSubject(emailSubject);
        mailDto.setTemplateName("email-template-reporter.ftl");
        Map<String, Object> context = new HashMap<>();
        Date baseDay = Calendar.getInstance().getTime();
        DateUtils.setMonths(baseDay, LocalDateTime.now().getMonthValue() - 2);
        List<Date> timeWindow = monthlySettlementDayRuleService.getMonthlySettlementDateWindow(baseDay);
        List<AttendanceDTO4Email> attendanceDTOList = getAttendanceDTO4Emails(timeWindow.get(0), timeWindow.get(1), teamList);
        context.put("attendance", attendanceDTOList);
        context.put("approveUrl", internSystemUrl + "leader");
        mailDto.setModel(context);
        emailService.sendEmailWithTemplate(mailDto);
        logger.info(new Gson().toJson(mailDto));
    }

    private String getAdminEmail() {
        return String.join(";", profileDomService.findUserByRole(Role.SUPER_ADMIN).stream().
                    map(User::getEmail).collect(Collectors.toList()));
    }

    private String getEmailTo() {
        String teamLeaderEmails = String.join(";", profileDomService.findUserByRole(Role.TEAM_LEADER).stream().
                map(User::getEmail).collect(Collectors.toList()));
        String hrEmails = String.join(";", profileDomService.findUserByRole(Role.HR).stream().
                map(User::getEmail).collect(Collectors.toList()));
        return teamLeaderEmails + ";" + hrEmails;
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
