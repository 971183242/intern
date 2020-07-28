package com.demo.workshop.intern.domain.attendance.entity;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PeriodAttendance {
    private String internId;
    private Date startDate;
    private Date endDate;
    private List<DailyAttendance> attendances = Lists.newArrayList();
    private int checkedInAttendanceCount;
    private int approvedAttendanceCount;
    private int rejectedAttendanceCount;
}
