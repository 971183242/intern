package com.oocl.workshop.intern.domain.attendance.repostitory.facade;

import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AttendanceRepoTest {

    @Autowired
    AttendanceRepo attendanceRepo;

    @BeforeAll
    public void before() {
        AttendancePo attendancePo0 = new AttendancePo();
        attendancePo0.setAttendanceStatus(AttendanceStatus.CheckedIn);
        attendancePo0.setInternId(1L);
        attendancePo0.setWorkDay(new Date(120, 0, 1));
        attendanceRepo.save(attendancePo0);

        AttendancePo attendancePo1 = new AttendancePo();
        attendancePo1.setAttendanceStatus(AttendanceStatus.Approved);
        attendancePo1.setInternId(1L);
        attendancePo1.setWorkDay(new Date(120, 3, 1));
        attendanceRepo.save(attendancePo1);

        AttendancePo attendancePo2 = new AttendancePo();
        attendancePo2.setAttendanceStatus(AttendanceStatus.Rejected);
        attendancePo2.setInternId(2L);
        attendancePo2.setWorkDay(new Date(120, 5, 1));
        attendanceRepo.save(attendancePo2);
    }


    @Test
    public void findByWorkdayBetween() {
        List<AttendancePo> attendancePos = attendanceRepo.findByWorkDayBetween(new Date(120, 1, 1), new Date(120, 5, 1));
        assertEquals(2, attendancePos.size());
    }

    @Test
    public void findByWorkDayBetween() {
        List<AttendancePo> attendancePos = attendanceRepo.findByInternId(1L);
        assertEquals(2, attendancePos.size());
    }

    @Test
    public void findByInternIdAndWorkDayBetween() {
        List<AttendancePo> attendancePos = attendanceRepo.findByInternIdAndWorkDayBetween(1l, new Date(120, 1, 1), new Date(120, 5, 1));
        assertEquals(1, attendancePos.size());
    }
}