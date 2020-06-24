package com.oocl.workshop.intern.domain.attendance.service;

import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;

public interface AttendanceService {

    DailyAttendance createAttendance(DailyAttendance attendance);

    DailyAttendance updateAttendance(DailyAttendance attendance);

    DailyAttendance removeAttendance(DailyAttendance attendance);

    DailyAttendance rejectAttendance(DailyAttendance attendance);

    DailyAttendance approveAttendance(DailyAttendance attendance);

    PeriodAttendance approveMonthlyAttendance(PeriodAttendance periodAttendance);
}
