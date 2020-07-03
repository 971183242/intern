package com.oocl.workshop.intern.domain.attendance.repostitory.facade;

import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AttendanceRepo extends JpaRepository<AttendancePo, Long> {
    List<AttendancePo> findByInternIdAndWorkDayBetweenOrderByWorkDay(String internId, Date startDate, Date endDate);
    List<AttendancePo> findByInternIdAndAttendanceStatus(String internId, AttendanceStatus status);
}
