package com.oocl.workshop.intern.domain.attendance.service;

import com.oocl.workshop.intern.domain.attendance.entity.Attendance;

import java.util.Date;
import java.util.List;

public interface AttendanceStatusService {
    List<Attendance> findAttendancesByInternIdAndDate(Long internId, Date dateFrom, Date dateTo);

    Attendance createUpdateAttendance(Attendance attendance);

}
