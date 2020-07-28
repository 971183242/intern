package com.demo.workshop.intern.domain.report.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AttendanceSummary {

    private float approvedAttendanceCount;
    private Date attendanceDateFrom;
    private Date attendanceDateTo;
    private float attendanceDays;
    private String internDomainId;
    private String internName;
    private float notApprovedAttendanceCount;
    private float rejectedAttendanceCount;
    private int teamLeaderDomainId;
    private int teamLeaderName;
    private int teamName;

}