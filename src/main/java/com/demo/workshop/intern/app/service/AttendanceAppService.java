package com.demo.workshop.intern.app.service;

import com.demo.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.demo.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.demo.workshop.intern.domain.profile.entity.Intern;

import java.util.Date;
import java.util.List;

public interface AttendanceAppService {
    DailyAttendance checkIn(String internId, Date date);

    void cancelCheckIn(long id);

    boolean containsUnconfirmedAttendance(String internId);

    PeriodAttendance findAttendances(String internId, Date date);

    List<Intern> getInternsActiveInDateContainedPeriod(String teamId, Date date);

    PeriodAttendance confirmPeriodAttendance(PeriodAttendance periodAttendance);
}
