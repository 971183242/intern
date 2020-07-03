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

}