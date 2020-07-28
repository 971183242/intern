package com.demo.workshop.intern.task;

import com.demo.workshop.intern.domain.report.repostitory.facade.MonthlySettlementDayRuleRepo;
import com.demo.workshop.intern.support.ActiveMQConfig;
import com.demo.workshop.intern.support.common.event.DomainEvent;
import com.demo.workshop.intern.support.common.event.EventPublisher;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;


@Component
public class PeriodAttendanceReporterTask {

    Logger logger = LoggerFactory.getLogger(PeriodAttendanceReporterTask.class);

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private MonthlySettlementDayRuleRepo ruleRepo;

//        @Scheduled(cron = "0/5 * * * * MON-FRI ")
    @Scheduled(cron = "0 45 8 * * MON-FRI ")
    public void execute() {
        if (LocalDate.now().getDayOfMonth() - ruleRepo.getMonthlySettlementDay() > 3) {
            DomainEvent event = new DomainEvent();
            event.setId(UUID.randomUUID().toString());
            event.setTimestamp(new Date());
            event.setSource("attendanceReport");
            eventPublisher.publish(event, ActiveMQConfig.REPORT_QUEUE);
            logger.info("AttendanceReporterTask" + new Gson().toJson(event));
        }
    }
}
