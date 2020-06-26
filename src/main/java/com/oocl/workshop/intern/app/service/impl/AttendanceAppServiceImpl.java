package com.oocl.workshop.intern.app.service.impl;

import com.oocl.workshop.intern.app.service.AttendanceAppService;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus.CheckedIn;

@Service
public class AttendanceAppServiceImpl implements AttendanceAppService {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceAppServiceImpl(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @Override
    public DailyAttendance checkIn(String internId, Date date) {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setInternId(internId);
        attendance.setWorkDay(date);
        attendance.setAttendanceStatus(CheckedIn);
        return attendanceService.createAttendance(attendance);
    }

    @Override
    public DailyAttendance cancelCheckIn(long id) {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setAttendanceId(id);
        return attendanceService.removeAttendance(attendance);
    }
}
