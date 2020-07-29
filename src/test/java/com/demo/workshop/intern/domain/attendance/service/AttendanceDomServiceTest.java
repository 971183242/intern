package com.demo.workshop.intern.domain.attendance.service;

import com.demo.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.demo.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.demo.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.demo.workshop.intern.domain.attendance.repostitory.facade.AttendanceRepo;
import com.demo.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import com.demo.workshop.intern.domain.attendance.service.impl.AttendanceDomServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.time.DateUtils.isSameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    void findByInternIdAndStatus() {
        String internId = "1";
        AttendancePo attendancePo1 = new AttendancePo();
        attendancePo1.setInternId(internId);
        attendancePo1.setAttendanceStatus(AttendanceStatus.CheckedIn);
        DailyAttendance attendance1 = new DailyAttendance();
        attendance1.setInternId(internId);
        attendance1.setAttendanceStatus(AttendanceStatus.CheckedIn);

        AttendancePo attendancePo2 = new AttendancePo();
        attendancePo2.setInternId(internId);
        attendancePo2.setAttendanceStatus(AttendanceStatus.CheckedIn);
        DailyAttendance attendance2 = new DailyAttendance();
        attendance2.setInternId(internId);
        attendance2.setAttendanceStatus(AttendanceStatus.CheckedIn);
        when(attendanceFactory.getAttendance(attendancePo1)).thenReturn(attendance1);
        when(attendanceFactory.getAttendance(attendancePo2)).thenReturn(attendance2);
        when(attendanceRepo.findByInternIdAndAttendanceStatus(internId, AttendanceStatus.CheckedIn)).thenReturn(Arrays.asList(attendancePo1, attendancePo2));
        List<DailyAttendance> dailyAttendanceList = attendanceDomService.findByInternIdAndStatus(internId, AttendanceStatus.CheckedIn);
        assertTrue(dailyAttendanceList.containsAll(Arrays.asList(attendance1, attendance2)));
        assertTrue(Arrays.asList(attendance1, attendance2).containsAll(dailyAttendanceList));
    }

    @Test
    void findByInternIdAndStatusEmptyTest() {
        String internId = "1";
        when(attendanceRepo.findByInternIdAndAttendanceStatus(internId, AttendanceStatus.CheckedIn)).thenReturn(Arrays.asList());
        List<DailyAttendance> dailyAttendanceList = attendanceDomService.findByInternIdAndStatus(internId, AttendanceStatus.CheckedIn);
        assertTrue(CollectionUtils.isEmpty(dailyAttendanceList));
    }

    @Test
    void confirmPeriodAttendance() {
        String internId = "1";
        LocalDateTime localDateTime = LocalDateTime.of(2020, Month.JUNE, 20, 10, 0);
        Date workDay = Date.from(localDateTime.toInstant(ZoneOffset.UTC));

        DailyAttendance approvedAttendance = new DailyAttendance();
        approvedAttendance.setInternId(internId);
        approvedAttendance.setWorkDay(workDay);
        approvedAttendance.setAttendanceStatus(AttendanceStatus.Approved);
        AttendancePo approvedAttendancePo = new AttendancePo();
        approvedAttendancePo.setInternId(internId);
        approvedAttendancePo.setWorkDay(workDay);
        approvedAttendancePo.setAttendanceStatus(AttendanceStatus.Approved);

        DailyAttendance rejectedAttendance = new DailyAttendance();
        rejectedAttendance.setAttendanceId(1L);
        rejectedAttendance.setInternId(internId);
        rejectedAttendance.setWorkDay(workDay);
        rejectedAttendance.setAttendanceStatus(AttendanceStatus.Rejected);
        AttendancePo rejectAttendancePo = new AttendancePo();
        rejectAttendancePo.setAttendanceId(1L);
        rejectAttendancePo.setInternId(internId);
        rejectAttendancePo.setWorkDay(workDay);
        rejectAttendancePo.setAttendanceStatus(AttendanceStatus.Rejected);

        DailyAttendance checkInAttendance = new DailyAttendance();
        checkInAttendance.setAttendanceId(1L);
        checkInAttendance.setInternId(internId);
        checkInAttendance.setWorkDay(workDay);
        checkInAttendance.setAttendanceStatus(AttendanceStatus.CheckedIn);
        AttendancePo checkInAttendancePo = new AttendancePo();
        checkInAttendancePo.setAttendanceId(1L);
        checkInAttendancePo.setInternId(internId);
        checkInAttendancePo.setWorkDay(workDay);
        checkInAttendancePo.setAttendanceStatus(AttendanceStatus.CheckedIn);

        when(attendanceFactory.createPo(approvedAttendance)).thenReturn(approvedAttendancePo);
        when(attendanceRepo.save(approvedAttendancePo)).thenReturn(approvedAttendancePo);
        when(attendanceFactory.getAttendance(approvedAttendancePo)).thenReturn(approvedAttendance);

        when(attendanceFactory.createPo(rejectedAttendance)).thenReturn(rejectAttendancePo);
        when(attendanceRepo.save(rejectAttendancePo)).thenReturn(rejectAttendancePo);
        when(attendanceFactory.getAttendance(rejectAttendancePo)).thenReturn(rejectedAttendance);

        PeriodAttendance periodAttendance = new PeriodAttendance();
        periodAttendance.getAttendances().add(approvedAttendance);
        periodAttendance.getAttendances().add(rejectedAttendance);
        periodAttendance.getAttendances().add(checkInAttendance);

        PeriodAttendance result = attendanceDomService.confirmPeriodAttendance(periodAttendance);

        List<DailyAttendance> approvedAttendances = result.getAttendances().stream().filter(dailyAttendance -> dailyAttendance.getAttendanceStatus().equals(AttendanceStatus.Approved)).collect(Collectors.toList());
        List<DailyAttendance> checkedInAttendances = result.getAttendances().stream().filter(dailyAttendance -> dailyAttendance.getAttendanceStatus().equals(AttendanceStatus.CheckedIn)).collect(Collectors.toList());
        List<DailyAttendance> rejectedAttendances = result.getAttendances().stream().filter(dailyAttendance -> dailyAttendance.getAttendanceStatus().equals(AttendanceStatus.Rejected)).collect(Collectors.toList());

        assertAll(() -> assertThat(approvedAttendances.size()).isEqualTo(1),
                () -> assertThat(checkedInAttendances.size()).isEqualTo(1),
                () -> assertThat(rejectedAttendances.size()).isEqualTo(1));


    }

    @Test
    void findByInternIdAndWorkDay() {
        long attendanceId = 1L;
        String internId = "1";
        Date workDay = new Date();
        AttendancePo attendancePo = new AttendancePo();
        DailyAttendance dailyAttendance = new DailyAttendance();
        dailyAttendance.setInternId(internId);
        dailyAttendance.setAttendanceStatus(AttendanceStatus.CheckedIn);
        dailyAttendance.setWorkDay(workDay);
        dailyAttendance.setAttendanceId(attendanceId);
        when(attendanceRepo.findByInternIdAndWorkDayBetweenOrderByWorkDay(internId, workDay, workDay)).thenReturn(Collections.singletonList(attendancePo));
        when(attendanceFactory.getAttendance(attendancePo)).thenReturn(dailyAttendance);
        Optional<DailyAttendance> resultOpt = attendanceDomService.findByInternIdAndWorkDay(internId, workDay);
        assertSame(resultOpt.get(), dailyAttendance);
    }

    @Test
    void findByInternIdAndWorkDayEmptyTest() {
        String internId = "1";
        Date workDay = new Date();
        when(attendanceRepo.findByInternIdAndWorkDayBetweenOrderByWorkDay(internId, workDay, workDay)).thenReturn(Arrays.asList());
        Optional<DailyAttendance> resultOpt = attendanceDomService.findByInternIdAndWorkDay(internId, workDay);
        assertFalse(resultOpt.isPresent());
    }


    @Test
    void getAttendance() {
        long attendanceId = 100L;
        AttendancePo attendancePo = new AttendancePo();
        attendancePo.setAttendanceId(attendanceId);
        when(attendanceRepo.findById(attendanceId)).thenReturn(Optional.of(attendancePo));
        DailyAttendance attendance = new DailyAttendance();
        when(attendanceFactory.getAttendance(attendancePo)).thenReturn(attendance);
        assertEquals(attendance, attendanceDomService.getAttendance(attendanceId));
    }

    @Test
    void getAttendanceWithoutRecord() {
        when(attendanceRepo.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(attendanceDomService.getAttendance(100L));
    }

    @Test
    void updateWithoutRecord() {
        when(attendanceRepo.findById(nullable(Long.class))).thenReturn(Optional.empty());
        DailyAttendance requestAttendance = new DailyAttendance();
        DailyAttendance updatedAttendance = attendanceDomService.updateAttendance(requestAttendance);
        assertNull(updatedAttendance);
        // too many mocks.......
    }

    // most ugly test ever written with such structure
    @Test
    void updateAttendance() {
        AttendancePo attendancePo = new AttendancePo();
        when(attendanceRepo.findById(nullable(Long.class))).thenReturn(Optional.of(attendancePo));
        DailyAttendance requestAttendance = new DailyAttendance();
        DailyAttendance expectedAttendance = new DailyAttendance();
        when(attendanceFactory.getAttendance(attendancePo)).thenReturn(expectedAttendance);
        assertEquals(expectedAttendance, attendanceDomService.updateAttendance(requestAttendance));
    }

    @Test
    void getPeriodAttendanceWithoutRecords() {
        String internId = "100";
        Date dateBegin = new Date();
        Date dateEnd = DateUtils.addMonths(dateBegin, 1);
        when(attendanceRepo.findByInternIdAndWorkDayBetweenOrderByWorkDay(internId, dateBegin, dateEnd))
                .thenReturn(ImmutableList.of());

        PeriodAttendance periodAttendance = attendanceDomService.getPeriodAttendance(internId, dateBegin, dateEnd);
        assertEquals(internId, periodAttendance.getInternId());
        assertTrue(isSameInstant(dateBegin, periodAttendance.getStartDate()));
        assertTrue(isSameInstant(dateEnd, periodAttendance.getEndDate()));
        assertTrue(periodAttendance.getAttendances().isEmpty());
        assertEquals(0, periodAttendance.getApprovedAttendanceCount());
        assertEquals(0, periodAttendance.getRejectedAttendanceCount());
        assertEquals(0, periodAttendance.getCheckedInAttendanceCount());
    }

    @Test
    void getPeriodAttendance() {
        String internId = "100";
        Date dateBegin = new Date();
        Date dateEnd = DateUtils.addMonths(dateBegin, 1);
        AttendancePo ap1 = getAttendancePo(AttendanceStatus.Approved);
        DailyAttendance da1 = getDailyAttendance(AttendanceStatus.Approved);
        AttendancePo ap2 = getAttendancePo(AttendanceStatus.Rejected);
        DailyAttendance da2 = getDailyAttendance(AttendanceStatus.Rejected);
        AttendancePo ap3 = getAttendancePo(AttendanceStatus.CheckedIn);
        DailyAttendance da3 = getDailyAttendance(AttendanceStatus.CheckedIn);
        AttendancePo ap4 = getAttendancePo(AttendanceStatus.CheckedIn);
        DailyAttendance da4 = getDailyAttendance(AttendanceStatus.CheckedIn);
        when(attendanceFactory.getAttendance(any())).thenReturn(da1).thenReturn(da2).thenReturn(da3).thenReturn(da4);
        when(attendanceRepo.findByInternIdAndWorkDayBetweenOrderByWorkDay(internId, dateBegin, dateEnd))
                .thenReturn(ImmutableList.of(ap1, ap2, ap3, ap4));

        PeriodAttendance periodAttendance = attendanceDomService.getPeriodAttendance(internId, dateBegin, dateEnd);

        assertEquals(internId, periodAttendance.getInternId());
        assertTrue(isSameInstant(dateBegin, periodAttendance.getStartDate()));
        assertTrue(isSameInstant(dateEnd, periodAttendance.getEndDate()));
        assertTrue(periodAttendance.getAttendances().containsAll(ImmutableSet.of(da1, da2, da3, da4)));
        assertEquals(4, periodAttendance.getAttendances().size());
        assertEquals(1, periodAttendance.getApprovedAttendanceCount());
        assertEquals(1, periodAttendance.getRejectedAttendanceCount());
        assertEquals(2, periodAttendance.getCheckedInAttendanceCount());
    }

    private AttendancePo getAttendancePo(AttendanceStatus attendanceStatus) {
        AttendancePo ap1 = new AttendancePo();
        ap1.setAttendanceStatus(attendanceStatus);
        return ap1;
    }

    private DailyAttendance getDailyAttendance(AttendanceStatus attendanceStatus) {
        DailyAttendance da1 = new DailyAttendance();
        da1.setAttendanceStatus(attendanceStatus);
        return da1;
    }
}
