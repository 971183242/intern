package com.demo.workshop.intern.domain.attendance.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DailyAttendance {

    private Long attendanceId;

    private String internId;

    private AttendanceStatus attendanceStatus;

    private Date workDay;

    private int version;

    private String createdBy;

    private Date createdDate;

    private String lastModifiedBy;

    private Date lastModifiedDate;
}
