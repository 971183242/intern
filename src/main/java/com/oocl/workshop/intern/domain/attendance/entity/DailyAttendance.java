package com.oocl.workshop.intern.domain.attendance.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
