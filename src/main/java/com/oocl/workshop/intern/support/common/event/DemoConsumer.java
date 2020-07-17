package com.oocl.workshop.intern.support.common.event;

import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceDomService;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomService;
import com.oocl.workshop.intern.domain.report.repostitory.po.MonthlySettlementDayRule;
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

import java.util.Date;
import java.util.List;

import static com.oocl.workshop.intern.support.ActiveMQConfig.INTERN_QUEUE;
import static com.oocl.workshop.intern.support.ActiveMQConfig.REPORT_QUEUE;

@Component
public class DemoConsumer {

    @Autowired
    private ProfileDomService profileDomService;

    @Autowired
    private AttendanceDomService attendanceDomService;

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
                                     Message message, Session session) {
        Date dateFrom = getFromDate();
        Date dateTo = getToDate();
        List<Team> teamList = profileDomService.findAllTeams();
        teamList.forEach(team -> {
            if (team.getTeamLeader() != null) {
                logger.info("TeamLeader: " + team.getTeamLeader().getName() + " Email: " + team.getTeamLeader().getEmail());
            }
            List<Intern> internList = profileDomService.findTeamInterns(team.getTeamId(), dateFrom, dateTo);
            internList.forEach(intern -> {
                logger.info("Intern: " + intern.getName() + " Email: " + intern.getEmail());
                logger.info("PeriodAttendance: ");
                PeriodAttendance periodAttendance = attendanceDomService.getPeriodAttendance(intern.getDomainId(), dateFrom, dateTo);
                periodAttendance.getAttendances().forEach(dailyAttendance -> {
                    logger.info(dailyAttendance.getAttendanceStatus().toString());
                });
            });
        });
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
