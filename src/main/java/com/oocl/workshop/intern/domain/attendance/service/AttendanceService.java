package com.oocl.workshop.intern.domain.attendance.service;

import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.MonthlyAttendance;

public interface AttendanceService {

    DailyAttendance createAttendance(DailyAttendance attendance);

    DailyAttendance updateAttendance(DailyAttendance attendance);

    DailyAttendance removeAttendance(DailyAttendance attendance);

    DailyAttendance rejectAttendance(DailyAttendance attendance);

    DailyAttendance approveAttendance(DailyAttendance attendance);

    MonthlyAttendance findMonthlyAttendance(String domainId, int year, int month);

    MonthlyAttendance approveMonthlyAttendance(MonthlyAttendance monthlyAttendance);
}
