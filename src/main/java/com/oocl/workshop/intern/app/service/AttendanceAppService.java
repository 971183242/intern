package com.oocl.workshop.intern.app.service;

import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;

import java.util.Date;

public interface AttendanceAppService {
    DailyAttendance checkIn(String internId, Date date);

    void cancelCheckIn(long id);

    DailyAttendance confirm(DailyAttendance requestAttendance);

    boolean containsUnconfirmedAttendance(String internId);

    PeriodAttendance findAttendances(String internId, Date date);
}
