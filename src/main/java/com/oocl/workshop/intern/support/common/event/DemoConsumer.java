package com.oocl.workshop.intern.support.common.event;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.oocl.workshop.intern.app.service.EmailService;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceDomService;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomService;
import com.oocl.workshop.intern.domain.report.repostitory.po.MonthlySettlementDayRule;
import com.oocl.workshop.intern.interfaces.dto.email.AttendanceDTO4Email;
import com.oocl.workshop.intern.interfaces.dto.email.MailSenderDTO;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.oocl.workshop.intern.support.ActiveMQConfig.INTERN_QUEUE;
import static com.oocl.workshop.intern.support.ActiveMQConfig.REPORT_QUEUE;

@Component
public class DemoConsumer {

    @Autowired
    private ProfileDomService profileDomService;

    @Autowired
    private AttendanceDomService attendanceDomService;

    @Autowired
    private EmailService emailService;

    private final Logger logger = LoggerFactory.getLogger(DemoConsumer.class);

    @JmsListener(destination = INTERN_QUEUE)
    public void receiveMessage(@Payload DomainEvent event,
                               @Headers MessageHeaders headers,
                               Message message, Session session) {
        System.out.println("received <" + event + ">");

        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.println("######          Message Details           #####");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.println("headers: " + headers);
        System.out.println("message: " + message);
        System.out.println("session: " + session);
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - -");
    }

    @JmsListener(destination = REPORT_QUEUE)
    public void receiveReportMessage(@Payload DomainEvent event,
                                     @Headers MessageHeaders headers,
                                     Message message, Session session) throws MessagingException, IOException, TemplateException {
        Date dateFrom = getFromDate();
        Date dateTo = getToDate();
        List<Team> teamList = profileDomService.findAllTeams();
        MailSenderDTO mailDto = new MailSenderDTO();
        mailDto.setFrom("will.l.y.wang@oocl.com");
        mailDto.setTo("will.l.y.wang@oocl.com");
        mailDto.setSubject("实习生管理系统-审批报表");
        mailDto.setCc("zack.tang@oocl.com");
        mailDto.setTemplateName("email-template-reporter.ftl");
        Map<String, Object> context = new HashMap<>();
        List<AttendanceDTO4Email> attendanceDTOList = getAttendanceDTO4Emails(dateFrom, dateTo, teamList);
        context.put("attendance", attendanceDTOList);
        mailDto.setModel(context);
        emailService.sendEmailWithTemplate(mailDto);
        logger.info(new Gson().toJson(mailDto));
    }

    private List<AttendanceDTO4Email> getAttendanceDTO4Emails(Date dateFrom, Date dateTo, List<Team> teamList) {
        List<AttendanceDTO4Email> attendanceDTOList = Lists.newArrayList();

        teamList.forEach(team -> {
            if (team.getTeamLeader() != null) {
                logger.info("TeamLeader: " + team.getTeamLeader().getName() + " Email: " + team.getTeamLeader().getEmail());
            }
            List<Intern> internList = profileDomService.findTeamInterns(team.getTeamId(), dateFrom, dateTo);
            internList.forEach(intern -> {
                PeriodAttendance periodAttendance = attendanceDomService.getPeriodAttendance(intern.getDomainId(), dateFrom, dateTo);
                AttendanceDTO4Email attendanceDTO = new AttendanceDTO4Email();
                String internId = periodAttendance.getInternId();
                attendanceDTO.setInternName(profileDomService.findUserByDomainId(internId).get().getName());
                attendanceDTO.setApprovedDays(periodAttendance.getApprovedAttendanceCount());
                attendanceDTO.setRejectedDays(periodAttendance.getRejectedAttendanceCount());
                attendanceDTO.setCheckInDays(periodAttendance.getCheckedInAttendanceCount());
                attendanceDTOList.add(attendanceDTO);
            });
        });
        return attendanceDTOList;
    }

    private Date getToDate() {
        Date dateTo = new Date();
        dateTo.setDate(MonthlySettlementDayRule.DEFAULT_DAY);
        return dateTo;
    }

    private Date getFromDate() {
        Date dateFrom = new Date();
        dateFrom.setMonth(dateFrom.getMonth() - 1);
        dateFrom.setTime(MonthlySettlementDayRule.DEFAULT_DAY);
        return dateFrom;
    }
}
