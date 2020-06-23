package com.oocl.workshop.intern.domain.attendance.service;

import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class AttendanceFactoryTest {
    @InjectMocks
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
}