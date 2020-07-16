package com.oocl.workshop.intern.task;

import com.google.gson.Gson;
import com.oocl.workshop.intern.support.ActiveMQConfig;
import com.oocl.workshop.intern.support.common.event.DomainEvent;
import com.oocl.workshop.intern.support.common.event.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;


@Component
public class AttendanceReporterTask {

    Logger logger = LoggerFactory.getLogger(AttendanceReporterTask.class);

    @Autowired
    private EventPublisher eventPublisher;

    @Scheduled(cron = "0/5 * *  * * MON-FRI ")
    public void attendanceReportTask() {
        DomainEvent event = new DomainEvent();
        event.setId(UUID.randomUUID().toString());
        event.setTimestamp(new Date());
        event.setSource("attendanceReport");
        eventPublisher.publish(event, ActiveMQConfig.REPORT_QUEUE);
        logger.info("AttendanceReporterTask" +  new Gson().toJson(event));
    }
}
