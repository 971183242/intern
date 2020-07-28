package com.demo.workshop.intern.domain.attendance.repostitory.facade;

import com.demo.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.demo.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
        attendancePo0.setInternId("intern_01");
        attendancePo0.setWorkDay(new Date(120, 0, 1));
        attendanceRepo.save(attendancePo0);

        AttendancePo attendancePo1 = new AttendancePo();
        attendancePo1.setAttendanceStatus(AttendanceStatus.Approved);
        attendancePo1.setInternId("intern_02");
        attendancePo1.setWorkDay(new Date(120, 3, 1));
        attendanceRepo.save(attendancePo1);

        AttendancePo attendancePo2 = new AttendancePo();
        attendancePo2.setAttendanceStatus(AttendanceStatus.Rejected);
        attendancePo2.setInternId("intern_02");
        attendancePo2.setWorkDay(new Date(120, 5, 1));
        attendanceRepo.save(attendancePo2);
    }


    @Test
    public void findByInternIdAndWorkDayBetween() {
        List<AttendancePo> attendancePos = attendanceRepo.findByInternIdAndWorkDayBetweenOrderByWorkDay("intern_02", new Date(120, 1, 1), new Date(120, 5, 1));
        assertEquals(2, attendancePos.size());
    }

    @Test
    @Sql(statements="insert into T_ATTENDANCE(attendance_id,intern_id, attendance_status,work_day,version)" +
            " values(9999998, 'test','CheckedIn','2020-05-01',12)")
    void updateVersionException() {

        AttendancePo attendancePo = new AttendancePo();
        attendancePo.setAttendanceId(9999998L);
        attendancePo.setInternId("test");
        attendancePo.setVersion(11);
        attendancePo.setAttendanceStatus(AttendanceStatus.Approved);

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> attendanceRepo.save(attendancePo));

    }
}