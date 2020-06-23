package com.oocl.workshop.intern.domain.attendance.service.impl;

import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.MonthlyAttendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.facade.AttendanceRepo;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceFactory;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceFactory attendanceFactory;

    @Autowired
    private AttendanceRepo attendanceRepo;


    @Override
    public DailyAttendance createAttendance(DailyAttendance attendance) {
        AttendancePo attendancePo = attendanceFactory.createPo(attendance);
        attendancePo = attendanceRepo.save(attendancePo);
        return attendanceFactory.getAttendance(attendancePo);
    }

    @Override
    public DailyAttendance updateAttendance(DailyAttendance attendance) {
        AttendancePo attendancePo = attendanceFactory.createPo(attendance);
        attendancePo = attendanceRepo.save(attendancePo);
        return attendanceFactory.getAttendance(attendancePo);
    }

    @Override
    public DailyAttendance removeAttendance(DailyAttendance attendance) {
        AttendancePo attendancePo = attendanceFactory.createPo(attendance);
        attendanceRepo.delete(attendancePo);
        return attendance;
    }

    @Override
    public DailyAttendance rejectAttendance(DailyAttendance attendance) {
        AttendancePo attendancePo = attendanceFactory.createPo(attendance);
        attendancePo.setAttendanceStatus(AttendanceStatus.Rejected);
        attendancePo = attendanceRepo.save(attendancePo);
        return attendanceFactory.getAttendance(attendancePo);
    }

    @Override
    public DailyAttendance approveAttendance(DailyAttendance attendance) {
        AttendancePo attendancePo = attendanceFactory.createPo(attendance);
        attendancePo.setAttendanceStatus(AttendanceStatus.Approved);
        attendancePo = attendanceRepo.save(attendancePo);
        return attendanceFactory.getAttendance(attendancePo);
    }

    @Override
    public MonthlyAttendance findMonthlyAttendance(String domainId, int year, int month) {
        MonthlyAttendance monthlyAttendance = attendanceFactory.getMonthlyAttendance(domainId, year, month);
        return monthlyAttendance;
    }

    @Override
    public MonthlyAttendance approveMonthlyAttendance(MonthlyAttendance monthlyAttendance) {
        List<DailyAttendance> attendanceList = monthlyAttendance.getAttendances().stream()
                .map(this::approveDailyCheckedInAttendance)
                .collect(Collectors.toList());

        monthlyAttendance.setAttendances(attendanceList);
        return monthlyAttendance;
    }

    private DailyAttendance approveDailyCheckedInAttendance(DailyAttendance dailyAttendance) {
        if (! AttendanceStatus.CheckedIn
                .equals(dailyAttendance.getAttendanceStatus())) {
            return dailyAttendance;
        }

        return approveAttendance(dailyAttendance);
    }
}