package com.oocl.workshop.intern.app.service;

import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;

import java.util.Date;

public interface AttendanceAppService {
    DailyAttendance checkIn(String internId, Date date);

    DailyAttendance cancelCheckIn(long id);
}
