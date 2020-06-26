package com.oocl.workshop.intern.app.service;

import com.oocl.workshop.intern.app.service.impl.AttendanceAppServiceImpl;
import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AttendanceAppServiceTest {
    @Mock
    AttendanceService attendanceService;

    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_checkIn_create_attendanceDo() {
        AttendanceAppService service = new AttendanceAppServiceImpl(attendanceService);
        Date date = parseDate("20200624");

        when(attendanceService.createAttendance(Mockito.any())).thenReturn(new DailyAttendance());
        DailyAttendance attendance = service.checkIn("guda", date);
        ArgumentCaptor<DailyAttendance> dailyAttendanceArgumentCaptor = ArgumentCaptor.forClass(DailyAttendance.class);
        verify(attendanceService, only()).createAttendance(dailyAttendanceArgumentCaptor.capture());

        assertEquals("guda", dailyAttendanceArgumentCaptor.getValue().getInternId());
        assertEquals(date, dailyAttendanceArgumentCaptor.getValue().getWorkDay());
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
        AttendanceAppService service = new AttendanceAppServiceImpl(attendanceService);
        when(attendanceService.createAttendance(Mockito.any())).thenReturn(mockAttendance);
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
        DailyAttendance attendance = new DailyAttendance();
        attendance.setAttendanceId(1L);
        when(attendanceService.removeAttendance(Mockito.any())).thenReturn(attendance);
        AttendanceAppServiceImpl attendanceAppService = new AttendanceAppServiceImpl(attendanceService);
        DailyAttendance removedAttendance = attendanceAppService.cancelCheckIn(1L);
        ArgumentCaptor<DailyAttendance> dailyAttendanceArgumentCaptor = ArgumentCaptor.forClass(DailyAttendance.class);
        verify(attendanceService, only()).removeAttendance(dailyAttendanceArgumentCaptor.capture());
        assertEquals(1L, dailyAttendanceArgumentCaptor.getValue().getAttendanceId());
        assertEquals(1L, removedAttendance.getAttendanceId());
    }
}
