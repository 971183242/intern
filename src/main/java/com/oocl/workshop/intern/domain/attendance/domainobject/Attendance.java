package com.oocl.workshop.intern.domain.attendance.domainobject;

import lombok.Data;

import java.util.Date;

@Data
public class Attendance {
    private Long domainId;
    private Long internId;
    private AttendanceStatus attendanceStatus;
    private Date workDay;
}
