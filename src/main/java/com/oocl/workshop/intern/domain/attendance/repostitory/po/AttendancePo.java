package com.oocl.workshop.intern.domain.attendance.repostitory.po;

import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "T_ATTENDANCE")
public class AttendancePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    private Long internId;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    @Temporal(TemporalType.DATE)
    private Date workDay;
}
