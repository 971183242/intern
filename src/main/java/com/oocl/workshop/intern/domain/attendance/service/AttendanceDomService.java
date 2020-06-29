package com.oocl.workshop.intern.domain.attendance.service;

import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;

import java.util.Date;

public interface AttendanceDomService {

    DailyAttendance createAttendance(String internId, Date workDay);

    void removeAttendance(long attendanceId);

    PeriodAttendance confirmPeriodAttendance(PeriodAttendance periodAttendance);
}
