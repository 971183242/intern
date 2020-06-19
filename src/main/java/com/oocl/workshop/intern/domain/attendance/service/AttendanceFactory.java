package com.oocl.workshop.intern.domain.attendance.service;

import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.MonthlyAttendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.facade.AttendanceRepo;
import com.oocl.workshop.intern.domain.attendance.repostitory.facade.MonthlySettlementDayRuleRepo;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceFactory {

    @Autowired
    AttendanceRepo attendanceRepo;

    @Autowired
    MonthlySettlementDayRuleRepo monthlySettlementDayRuleRepo;

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


    public MonthlyAttendance getMonthlyAttendance(String internId, int year, int month) {
        int monthlySettlementDay = monthlySettlementDayRuleRepo.getMonthlySettlementDay();

        LocalDate endLocalDate = LocalDate.of(year, month, monthlySettlementDay);
        LocalDate startLocalDate = endLocalDate.minus(1, ChronoUnit.MONTHS).plus(1, ChronoUnit.DAYS);
        Date startDate = localDate2Date(startLocalDate);
        Date endDate = localDate2Date(endLocalDate);

        List<AttendancePo> attendancePoList = attendanceRepo.findByInternIdAndWorkDayBetweenOrderByWorkDay(internId, startDate, endDate);
        List<DailyAttendance> dailyAttendances = attendancePoList.stream().map(this::getAttendance).collect(Collectors.toList());

        MonthlyAttendance monthlyAttendance = new MonthlyAttendance();
        monthlyAttendance.setInternId(internId);
        monthlyAttendance.setStartDate(startDate);
        monthlyAttendance.setEndDate(endDate);
        monthlyAttendance.setYear(year);
        monthlyAttendance.setMonth(month);
        monthlyAttendance.setAttendances(dailyAttendances);
        return monthlyAttendance;
    }


    private Date localDate2Date(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }
}
