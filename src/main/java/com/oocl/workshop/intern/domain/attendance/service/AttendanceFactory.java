package com.oocl.workshop.intern.domain.attendance.service;

import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.facade.AttendanceRepo;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceFactory {

    @Autowired
    AttendanceRepo attendanceRepo;

    public AttendancePo createPo(DailyAttendance attendance) {
        AttendancePo attendancePo = new AttendancePo();
        attendancePo.setAttendanceId(attendance.getAttendanceId());
        attendancePo.setAttendanceStatus(attendance.getAttendanceStatus());
        attendancePo.setInternId(attendance.getInternId());
        attendancePo.setWorkDay(attendance.getWorkDay());
        return attendancePo;
    }

    public DailyAttendance getAttendance(AttendancePo attendancePo) {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setAttendanceId(attendancePo.getAttendanceId());
        attendance.setInternId(attendancePo.getInternId());
        attendance.setWorkDay(attendancePo.getWorkDay());
        attendance.setAttendanceStatus(attendancePo.getAttendanceStatus());
        return attendance;
    }
}
