package com.oocl.workshop.intern.task;

import com.google.gson.Gson;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceFactory;
import com.oocl.workshop.intern.support.common.event.DomainEvent;
import com.oocl.workshop.intern.support.common.event.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;


@Component
public class AttendanceReporterTask {

    Logger logger = LoggerFactory.getLogger(AttendanceReporterTask.class);

    @Scheduled(cron = "0/5 * *  * * MON-FRI ")
    public void attendanceReportTask() {
        EventPublisher publisher = new EventPublisher();
        DomainEvent event = new DomainEvent();
        event.setId(UUID.randomUUID().toString());
        event.setTimestamp(new Date());
        event.setSource("attendanceReport");
//        publisher.triggerReportEvent(event);
        logger.info("AttendanceReporterTask" +  new Gson().toJson(event));
        System.out.println("attendanceReportTask");
    }
}
