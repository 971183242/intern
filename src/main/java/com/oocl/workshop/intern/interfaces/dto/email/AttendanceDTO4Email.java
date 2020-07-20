package com.oocl.workshop.intern.interfaces.dto.email;

import com.oocl.workshop.intern.interfaces.dto.attendance.AttendanceDTO;
import lombok.Data;

@Data
public class AttendanceDTO4Email extends AttendanceDTO {
    private String internName;
    private int checkInDays;
    private int approvedDays;
    private int rejectedDays;
}
