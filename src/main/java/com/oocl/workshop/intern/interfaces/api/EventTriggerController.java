package com.oocl.workshop.intern.interfaces.api;

import com.oocl.workshop.intern.task.DailyCheckInReminderTask;
import com.oocl.workshop.intern.task.PeriodAttendanceReporterTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventTriggerController {
    @Autowired
    private DailyCheckInReminderTask dailyCheckInReminderTask;

    @Autowired
    private PeriodAttendanceReporterTask periodAttendanceReporterTask;

    @GetMapping("/triggerDailyCheckInEvent")
    public void triggerDailyCheckInEvent() {
        dailyCheckInReminderTask.execute();
    }

    @GetMapping("/triggerPeriodAttendanceReportEvent")
    public void triggerPeriodAttendanceReportEvent() {
        periodAttendanceReporterTask.execute();
    }
}
