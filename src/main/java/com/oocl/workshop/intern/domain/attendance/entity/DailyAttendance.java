package com.oocl.workshop.intern.domain.attendance.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DailyAttendance {

    private Long attendanceId;

    private String internId;

    private AttendanceStatus attendanceStatus;

    private Date workDay;
}
