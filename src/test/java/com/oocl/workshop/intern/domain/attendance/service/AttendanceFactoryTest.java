package com.oocl.workshop.intern.domain.attendance.service;

import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.MonthlyAttendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.facade.AttendanceRepo;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(propagation = Propagation.NESTED)
@TestPropertySource("classpath:application-test.properties")
class AttendanceFactoryTest {

    @Autowired
    AttendanceRepo attendanceRepo;

    @Autowired
    AttendanceFactory attendanceFactory;

    @Test
    void createPo() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setAttendanceId(1l);
        attendance.setInternId("intern_01");
        attendance.setAttendanceStatus(AttendanceStatus.CheckedIn);
        attendance.setWorkDay(new Date(120, 0, 1));

        AttendancePo attendancePo = attendanceFactory.createPo(attendance);
        assertEquals(attendance.getAttendanceId(), attendancePo.getAttendanceId());
        assertEquals(attendance.getInternId(), attendancePo.getInternId());
        assertEquals(attendance.getAttendanceStatus(), attendancePo.getAttendanceStatus());
        assertEquals(attendance.getWorkDay(), attendancePo.getWorkDay());

        attendance.setAttendanceId(null);
        attendancePo = attendanceFactory.createPo(attendance);
        assertNull(attendancePo.getAttendanceId());
    }

    @Test
    void getAttendance() {
        AttendancePo attendancePo = new AttendancePo();
        attendancePo.setAttendanceId(1l);
        attendancePo.setInternId("intern_01");
        attendancePo.setAttendanceStatus(AttendanceStatus.CheckedIn);
        attendancePo.setWorkDay(new Date(120, 0, 1));

        DailyAttendance attendance = attendanceFactory.getAttendance(attendancePo);
        assertEquals(attendancePo.getAttendanceId(), attendance.getAttendanceId());
        assertEquals(attendancePo.getInternId(), attendance.getInternId());
        assertEquals(attendancePo.getAttendanceStatus(), attendance.getAttendanceStatus());
        assertEquals(attendancePo.getWorkDay(), attendance.getWorkDay());
    }

    @Test
    void getMonthlyAttendance() {
        AttendancePo attendancePo0 = new AttendancePo();
        attendancePo0.setInternId("intern_01");
        attendancePo0.setAttendanceStatus(AttendanceStatus.CheckedIn);
        attendancePo0.setWorkDay(new Date(120, 4, 25));
        attendanceRepo.save(attendancePo0);

        AttendancePo attendancePo1 = new AttendancePo();
        attendancePo1.setInternId("intern_01");
        attendancePo1.setAttendanceStatus(AttendanceStatus.Approved);
        attendancePo1.setWorkDay(new Date(120, 4, 28));
        attendanceRepo.save(attendancePo1);

        AttendancePo attendancePo2 = new AttendancePo();
        attendancePo2.setInternId("intern_01");
        attendancePo2.setAttendanceStatus(AttendanceStatus.Rejected);
        attendancePo2.setWorkDay(new Date(120, 5, 1));
        attendanceRepo.save(attendancePo2);

        MonthlyAttendance monthlyAttendance = attendanceFactory.getMonthlyAttendance("intern_01", 2020, 6);
        assertEquals("intern_01", monthlyAttendance.getInternId());
        assertEquals(2020, monthlyAttendance.getYear());
        assertEquals(6, monthlyAttendance.getMonth());
        assertEquals(new Date(120, 4, 26), monthlyAttendance.getStartDate());
        assertEquals(new Date(120, 5, 25), monthlyAttendance.getEndDate());

        assertEquals(2, monthlyAttendance.getAttendances().size());
        assertEquals("intern_01", monthlyAttendance.getAttendances().get(0).getInternId());
        assertEquals(AttendanceStatus.Approved, monthlyAttendance.getAttendances().get(0).getAttendanceStatus());
        assertEquals(new Date(120, 4, 28), monthlyAttendance.getAttendances().get(0).getWorkDay());
    }
}