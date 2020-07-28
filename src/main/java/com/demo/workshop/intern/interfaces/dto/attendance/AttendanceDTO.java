package com.demo.workshop.intern.interfaces.dto.attendance;

import lombok.Data;

@Data
public class AttendanceDTO {
    private Long attendanceId;

    private String internId;

    private String attendanceStatus;

    private String workDay;

    private int version;
}
