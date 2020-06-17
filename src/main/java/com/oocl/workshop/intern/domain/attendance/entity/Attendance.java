package com.oocl.workshop.intern.domain.attendance.entity;

import lombok.Data;

import java.time.Month;
import java.util.Date;

@Data
public class Attendance {
    private Long attendanceId;

    private Long internId;

    private AttendanceStatus attendanceStatus;

    private Date workDay;

    private Month month;
}
