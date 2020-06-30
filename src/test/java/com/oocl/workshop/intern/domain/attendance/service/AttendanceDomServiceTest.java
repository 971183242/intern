package com.oocl.workshop.intern.domain.attendance.service;

import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.facade.AttendanceRepo;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import com.oocl.workshop.intern.domain.attendance.service.impl.AttendanceDomServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceDomServiceTest {

    @InjectMocks
    private AttendanceDomService attendanceDomService = new AttendanceDomServiceImpl();

    @Mock
    private AttendanceRepo attendanceRepo;
    @Mock
    private AttendanceFactory attendanceFactory;

    @Test
    @DisplayName("create CheckIn status Attendance")
    void createAttendance() {
        String internId = "testId";
        LocalDateTime localDateTime = LocalDateTime.of(2020, Month.JUNE, 20, 10, 0);
        Date workDay = Date.from(localDateTime.toInstant(ZoneOffset.UTC));

        DailyAttendance attendanceWithoutId = new DailyAttendance();
        attendanceWithoutId.setInternId(internId);
        attendanceWithoutId.setWorkDay(workDay);
        attendanceWithoutId.setAttendanceStatus(AttendanceStatus.CheckedIn);

        DailyAttendance attendanceWithId = new DailyAttendance();
        attendanceWithId.setAttendanceId(1L);
        attendanceWithId.setInternId(internId);
        attendanceWithId.setWorkDay(workDay);
        attendanceWithId.setAttendanceStatus(AttendanceStatus.CheckedIn);

        AttendancePo attendancePoWithoutId = new AttendancePo();
        attendancePoWithoutId.setInternId(internId);
        attendancePoWithoutId.setWorkDay(workDay);
        attendancePoWithoutId.setAttendanceStatus(AttendanceStatus.CheckedIn);

        AttendancePo attendancePoWithId = new AttendancePo();
        attendancePoWithId.setAttendanceId(1L);
        attendancePoWithId.setInternId(internId);
        attendancePoWithId.setWorkDay(workDay);
        attendancePoWithId.setAttendanceStatus(AttendanceStatus.CheckedIn);

        when(attendanceFactory.createPo(attendanceWithoutId)).thenReturn(attendancePoWithoutId);
        when(attendanceRepo.save(attendancePoWithoutId)).thenReturn(attendancePoWithId);
        when(attendanceFactory.getAttendance(attendancePoWithId)).thenReturn(attendanceWithId);

        DailyAttendance attendance = attendanceDomService.createAttendance(internId, workDay);
        assertAll(() -> assertThat(attendance.getAttendanceId()).isEqualTo(1),
                () -> assertThat(attendance.getAttendanceStatus()).isEqualTo(AttendanceStatus.CheckedIn)
                );
    }


    @Test
    void confirmPeriodAttendance() {
        PeriodAttendance periodAttendance = new PeriodAttendance();
        String internId = "testId";

        LocalDateTime approvedDateTime = LocalDateTime.of(2020, Month.JUNE, 1, 10, 0);
        Date approvedDay = Date.from(approvedDateTime.toInstant(ZoneOffset.UTC));
        DailyAttendance approvedAttendance = attendanceDomService.createAttendance(internId, approvedDay);
        approvedAttendance.setAttendanceStatus(AttendanceStatus.Approved);
        periodAttendance.getAttendances().add(approvedAttendance);

        LocalDateTime approvedDateTime2 = LocalDateTime.of(2020, Month.JUNE, 2, 10, 0);
        Date approvedDay2 = Date.from(approvedDateTime2.toInstant(ZoneOffset.UTC));
        DailyAttendance approvedAttendance2 = attendanceDomService.createAttendance(internId, approvedDay2);
        approvedAttendance2.setAttendanceStatus(AttendanceStatus.Approved);
        periodAttendance.getAttendances().add(approvedAttendance2);

        LocalDateTime checkedInDateTime = LocalDateTime.of(2020, Month.JUNE, 3, 10, 0);
        Date checkedInDay = Date.from(approvedDateTime.toInstant(ZoneOffset.UTC));
        DailyAttendance checkedIndAttendance = attendanceDomService.createAttendance(internId, checkedInDay);
        periodAttendance.getAttendances().add(checkedIndAttendance);

        LocalDateTime rejectedDateTime = LocalDateTime.of(2020,Month.JUNE,5,10,0);
        Date rejectedDay = Date.from(approvedDateTime.toInstant(ZoneOffset.UTC));
        DailyAttendance rejectedAttendance = attendanceDomService.createAttendance(internId, rejectedDay);
        rejectedAttendance.setAttendanceStatus(AttendanceStatus.Rejected);
        periodAttendance.getAttendances().add(rejectedAttendance);

        periodAttendance = attendanceDomService.confirmPeriodAttendance(periodAttendance);

        PeriodAttendance finalPeriodAttendance = periodAttendance;
        assertAll(() -> assertThat(finalPeriodAttendance.getApprovedAttendanceCount()).isEqualTo(2),
                () -> assertThat(finalPeriodAttendance.getCheckedInAttendanceCount()).isEqualTo(1),
                () -> assertThat(finalPeriodAttendance.getRejectedAttendanceCount()).isEqualTo(1));


    }
}