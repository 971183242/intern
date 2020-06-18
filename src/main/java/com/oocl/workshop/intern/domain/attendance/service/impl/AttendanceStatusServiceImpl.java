package com.oocl.workshop.intern.domain.attendance.service.impl;

import com.oocl.workshop.intern.domain.attendance.entity.Attendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.facade.AttendanceRepo;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceFactory;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceStatusService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class AttendanceStatusServiceImpl implements AttendanceStatusService {

    @Autowired
    private AttendanceFactory attendanceFactory;

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Override
    public List<Attendance> findAttendancesByInternIdAndDate(Long internId, Date dateFrom, Date dateTo) {
        return null;
    }

    @Override
    public Attendance createAttendance(Attendance attendance) {
        AttendancePo attendancePo = attendanceFactory.createPo(attendance);
        attendancePo = attendanceRepo.save(attendancePo);
        return attendanceFactory.getAttendance(attendancePo);
    }

    @Override
    public Attendance updateAttendance(Attendance attendance) {
        return null;
    }

}
