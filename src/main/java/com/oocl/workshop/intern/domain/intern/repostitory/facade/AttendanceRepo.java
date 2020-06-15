package com.oocl.workshop.intern.domain.intern.repostitory.facade;

import com.oocl.workshop.intern.domain.intern.repostitory.po.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepo extends JpaRepository<Attendance, Long> {
}
