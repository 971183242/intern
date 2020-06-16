package com.oocl.workshop.intern.domain.attendance.repostitory.facade;

import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AttendanceRepo extends JpaRepository<AttendancePo, Long> {
    List<AttendancePo> findByWorkDayBetween(Date startDate, Date endDate);

    List<AttendancePo> findByInternId(long internId);

    List<AttendancePo> findByInternIdAndWorkDayBetween(long internId, Date startDate, Date endDate);
}
