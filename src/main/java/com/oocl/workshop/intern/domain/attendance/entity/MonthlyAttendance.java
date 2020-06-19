package com.oocl.workshop.intern.domain.attendance.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MonthlyAttendance {
    private String internId;
    private Date startDate;
    private Date endDate;
    private int year;
    private int month;
    private List<DailyAttendance> attendances;
}
