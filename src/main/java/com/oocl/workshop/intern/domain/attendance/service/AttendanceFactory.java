package com.oocl.workshop.intern.domain.attendance.service;

import com.oocl.workshop.intern.domain.attendance.entity.Attendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;

public class AttendanceFactory {
    public AttendancePo createPo(Attendance attendance){
        AttendancePo attendancePo = new AttendancePo();
        attendancePo.setAttendanceId(attendance.getAttendanceId());
        attendancePo.setAttendanceStatus(attendance.getAttendanceStatus());
        attendancePo.setInternId(attendance.getInternId());
        attendancePo.setWorkDay(attendance.getWorkDay());
        return attendancePo;
    }

    public Attendance getAttendance(AttendancePo attendancePo){
        return null;
    }
}
