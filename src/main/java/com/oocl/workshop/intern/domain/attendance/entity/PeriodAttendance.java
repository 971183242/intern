package com.oocl.workshop.intern.domain.attendance.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PeriodAttendance {
    private String internId;
    private Date startDate;
    private Date endDate;
    private List<DailyAttendance> attendances;
    private int checkedInAttendanceCount;
    private int approvedAttendanceCount;
    private int rejectedAttendanceCount;
}
