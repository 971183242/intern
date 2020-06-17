package com.oocl.workshop.intern.domain.attendance.service.impl;

import com.oocl.workshop.intern.domain.attendance.entity.Attendance;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceStatusService;

import java.util.Date;
import java.util.List;

public class AttendanceStatusServiceImpl implements AttendanceStatusService {
    @Override
    public List<Attendance> findAttendancesByInternIdAndDate(Long internId, Date dateFrom, Date dateTo) {
        return null;
    }
}
