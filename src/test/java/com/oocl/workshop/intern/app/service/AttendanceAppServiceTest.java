package com.oocl.workshop.intern.app.service;

import com.google.common.collect.ImmutableList;
import com.oocl.workshop.intern.app.service.impl.AttendanceAppServiceImpl;
import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.facade.AttendanceRepo;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceDomService;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomService;
import com.oocl.workshop.intern.domain.report.service.MonthlySettlementDayRuleService;
import com.oocl.workshop.intern.infrastructure.InternApplicationException;
import com.oocl.workshop.intern.infrastructure.common.ErrorCodes;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AttendanceAppServiceTest {
    @Mock
    AttendanceDomService attendanceDomService;

    @Mock
    MonthlySettlementDayRuleService monthlySettlementDayRuleService;
    @Mock
    private ProfileDomService profileDomService;

    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_checkIn_create_attendanceDo() {
        AttendanceAppServiceImpl service = new AttendanceAppServiceImpl();
        service.setAttendanceDomService(attendanceDomService);
        Date date = parseDate("20200624");

        when(attendanceDomService.createAttendance("guda", date)).thenReturn(new DailyAttendance());
        DailyAttendance attendance = service.checkIn("guda", date);
        assertNotNull(attendance);
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyyMMdd").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_checkIn_create_attendanceDo_from_domain_service() {
        Date date = parseDate("20200625");
        DailyAttendance mockAttendance = getMockedDailyAttendance(date);
        AttendanceAppServiceImpl service = new AttendanceAppServiceImpl();
        service.setAttendanceDomService(attendanceDomService);
        when(attendanceDomService.createAttendance("guda", date)).thenReturn(mockAttendance);
        DailyAttendance attendance = service.checkIn("guda", date);
        Assertions.assertNotEquals(0L, attendance.getAttendanceId());
        Assertions.assertEquals(mockAttendance.getAttendanceStatus(), attendance.getAttendanceStatus());
        Assertions.assertEquals(mockAttendance.getWorkDay(), attendance.getWorkDay());
        Assertions.assertEquals(mockAttendance.getInternId(), attendance.getInternId());
    }

    private DailyAttendance getMockedDailyAttendance(Date date) {
        DailyAttendance mockAttendance = new DailyAttendance();
        mockAttendance.setAttendanceId(1L);
        mockAttendance.setInternId("guda");
        mockAttendance.setAttendanceStatus(AttendanceStatus.CheckedIn);
        mockAttendance.setWorkDay(date);
        return mockAttendance;
    }

    @Test
    void should_cancel_checkIn_remove_domain_object_successfully() {
        AttendanceAppServiceImpl service = new AttendanceAppServiceImpl();
        service.setAttendanceDomService(attendanceDomService);
        service.cancelCheckIn(1L);
    }

    @Test
    void should_get_interns_from_domain_service() {
        AttendanceAppServiceImpl service = new AttendanceAppServiceImpl();
        service.setMonthlySettlementDayRuleService(monthlySettlementDayRuleService);
        service.setProfileDomService(profileDomService);

        Date date = parseDate("2020-06-29");
        Date startDate = parseDate("2020-06-01");
        Date endDate = parseDate("2020-06-30");
        when(monthlySettlementDayRuleService.getMonthlySettlementDateWindow(date)).thenReturn(Arrays.asList(startDate, endDate));

        List<Intern> mockedInterns = Arrays.asList(new Intern(), new Intern());
        when(profileDomService.findTeamInterns("team", startDate, endDate)).thenReturn(mockedInterns);
        List<Intern> interns = service.getInternsActiveInDateContainedPeriod("team", date);
        assertEquals(2, interns.size());
    }

    @Test
    void approveWithoutAttendanceRecord() {
        AttendanceAppServiceImpl service = new AttendanceAppServiceImpl();
        service.setAttendanceDomService(attendanceDomService);
        when(attendanceDomService.getAttendance(anyLong())).thenReturn(null);
        DailyAttendance attendance = new DailyAttendance();
        attendance.setAttendanceId(100L);
        attendance.setAttendanceStatus(AttendanceStatus.Approved);

        InternApplicationException exception = assertThrows(InternApplicationException.class, () -> service.confirm(attendance));
        assertEquals(ErrorCodes.ATTENDANCE_RECORD_NOT_FOUND, exception.getCode());
    }

    @Test
    void approveRejectedAttendance() {
        confirmAttendanceSuccess(AttendanceStatus.Rejected, AttendanceStatus.Approved);
    }

    @Test
    void approveApprovedAttendance() {
        confirmAttendanceSuccess(AttendanceStatus.Approved, AttendanceStatus.Approved);
    }

    @Test
    void rejectApprovedAttendance() {
        long attendanceId = 100L;
        DailyAttendance attendance = new DailyAttendance();
        attendance.setAttendanceStatus(AttendanceStatus.Approved);

        InternApplicationException exception = assertThrows(InternApplicationException.class,
                () -> confirmAttendance(AttendanceStatus.Rejected, attendanceId, attendance));
        assertEquals(ErrorCodes.TRY_TO_REJECT_APPROVED_ATTENDANCE, exception.getCode());
    }

    @Test
    void rejectAttendance() {
        confirmAttendanceSuccess(AttendanceStatus.CheckedIn, AttendanceStatus.Rejected);
    }

    @Test
    void useCheckinAsConfirmTargetStatus() {
        long attendanceId = 100L;
        DailyAttendance attendance = new DailyAttendance();
        attendance.setAttendanceStatus(AttendanceStatus.CheckedIn);

        InternApplicationException exception = assertThrows(InternApplicationException.class,
                () -> confirmAttendance(AttendanceStatus.CheckedIn, attendanceId, attendance));
        assertEquals(ErrorCodes.INVALID_ATTENDANCE_CONFIRM_STATUS, exception.getCode());
    }

    @Test
    void confirmWithoutSpecifedStatus() {
        long attendanceId = 100L;
        DailyAttendance attendance = new DailyAttendance();
        attendance.setAttendanceStatus(AttendanceStatus.CheckedIn);

        InternApplicationException exception = assertThrows(InternApplicationException.class,
                () -> confirmAttendance(null, attendanceId, attendance));
        assertEquals(ErrorCodes.INVALID_ATTENDANCE_CONFIRM_STATUS, exception.getCode());
    }

    private void confirmAttendanceSuccess(AttendanceStatus currentStatus, AttendanceStatus targetStatus) {
        long attendanceId = 100L;
        DailyAttendance attendance = new DailyAttendance();
        attendance.setAttendanceStatus(currentStatus);

        DailyAttendance updatedAttendance = confirmAttendance(targetStatus, attendanceId, attendance);
        assertEquals(targetStatus, updatedAttendance.getAttendanceStatus());
    }

    private DailyAttendance confirmAttendance(AttendanceStatus targetStatus, long attendanceId, DailyAttendance attendance) {
        AttendanceAppServiceImpl service = new AttendanceAppServiceImpl();
        service.setAttendanceDomService(attendanceDomService);
        when(attendanceDomService.getAttendance(attendanceId)).thenReturn(attendance);
        when(attendanceDomService.updateAttendance(any())).thenReturn(attendance);
        DailyAttendance requestAttendance = new DailyAttendance();
        requestAttendance.setAttendanceId(attendanceId);
        requestAttendance.setAttendanceStatus(targetStatus);

        return service.confirm(requestAttendance);
    }

    @Test
    void containsUnconfirmedAttendanceReturnsTrue() {
        String internId = "100";
        AttendanceAppServiceImpl service = new AttendanceAppServiceImpl();
        AttendanceRepo attendanceRepo = mock(AttendanceRepo.class);
        AttendancePo attendancePo = new AttendancePo();
        when(attendanceRepo.findByInternIdAndAttendanceStatus(internId, AttendanceStatus.CheckedIn)).thenReturn(ImmutableList.of(attendancePo));
        service.setAttendanceRepo(attendanceRepo);
        assertTrue(service.containsUnconfirmedAttendance(internId));
    }

    @Test
    void containsUnconfirmedAttendanceReturnsFalse() {
        String internId = "100";
        AttendanceAppServiceImpl service = new AttendanceAppServiceImpl();
        AttendanceRepo attendanceRepo = mock(AttendanceRepo.class);
        when(attendanceRepo.findByInternIdAndAttendanceStatus(internId, AttendanceStatus.CheckedIn)).thenReturn(ImmutableList.of());
        service.setAttendanceRepo(attendanceRepo);
        assertFalse(service.containsUnconfirmedAttendance(internId));
    }

    @Test
    void getAttendancesWithInvalidDate() {
        InternApplicationException e = new InternApplicationException(null);
        AttendanceAppServiceImpl service = new AttendanceAppServiceImpl();
        MonthlySettlementDayRuleService monthlySettlementDayRuleService = mock(MonthlySettlementDayRuleService.class);
        when(monthlySettlementDayRuleService.getMonthlySettlementDateWindow(ArgumentMatchers.isNull())).thenThrow(e);
        service.setMonthlySettlementDayRuleService(monthlySettlementDayRuleService);
        InternApplicationException exception = assertThrows(InternApplicationException.class, () -> service.findAttendances("100", null));
        assertEquals(e, exception);
    }

    @Test
    void getAttendances() {
        AttendanceAppServiceImpl service = new AttendanceAppServiceImpl();
        Date startDate = new Date();
        Date endDate = DateUtils.addMonths(startDate, 1);
        Date today = new Date();
        String internId = "100";
        MonthlySettlementDayRuleService monthlySettlementDayRuleService = mock(MonthlySettlementDayRuleService.class);
        when(monthlySettlementDayRuleService.getMonthlySettlementDateWindow(today)).thenReturn(ImmutableList.of(startDate, endDate));
        service.setMonthlySettlementDayRuleService(monthlySettlementDayRuleService);
        service.setAttendanceDomService(attendanceDomService);
        PeriodAttendance periodAttendance = new PeriodAttendance();
        when(attendanceDomService.getPeriodAttendance(internId, startDate, endDate)).thenReturn(periodAttendance);
        assertEquals(periodAttendance, service.findAttendances(internId, today));
        verify(attendanceDomService, Mockito.times(1)).getPeriodAttendance(internId, startDate, endDate);
    }
}
