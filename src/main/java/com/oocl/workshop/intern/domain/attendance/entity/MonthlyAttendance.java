package com.oocl.workshop.intern.domain.attendance.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MonthlyAttendance implements Attendance {
    private String internId;
    private Date startDate;
    private Date endDate;
    private int year;
    private int month;
    private List<DailyAttendance> attendances;

    public void setInternId(String internId) {
        this.internId = internId;
    }

    public String getInternId() {
        return internId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setAttendances(List<DailyAttendance> attendances) {
        this.attendances = attendances;
    }

    public List<DailyAttendance> getAttendances() {
        return attendances;
    }
}
