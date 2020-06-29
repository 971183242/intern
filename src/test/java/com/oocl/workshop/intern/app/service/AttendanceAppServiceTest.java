package com.oocl.workshop.intern.app.service;

import com.oocl.workshop.intern.app.service.impl.AttendanceAppServiceImpl;
import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceDomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class AttendanceAppServiceTest {
    @Mock
    AttendanceDomService attendanceDomService;

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
}
