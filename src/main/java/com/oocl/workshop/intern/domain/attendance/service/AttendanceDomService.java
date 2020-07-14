package com.oocl.workshop.intern.domain.attendance.service;

import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;

import java.util.Date;
import java.util.List;

public interface AttendanceDomService {

    DailyAttendance createAttendance(String internId, Date workDay);

    void removeAttendance(long attendanceId);

    PeriodAttendance confirmPeriodAttendance(PeriodAttendance periodAttendance);

    DailyAttendance getAttendance(long attendanceId);

    List<DailyAttendance> findByInternIdAndStatus(String internId, AttendanceStatus status);

    DailyAttendance updateAttendance(DailyAttendance dailyAttendance);

    PeriodAttendance getPeriodAttendance(String internId, Date startDate, Date endDate);
}
