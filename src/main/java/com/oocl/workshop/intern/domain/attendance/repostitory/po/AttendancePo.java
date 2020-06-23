package com.oocl.workshop.intern.domain.attendance.repostitory.po;

import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "T_ATTENDANCE", indexes = {
        @Index(name = "idx_attendace_day", columnList = "work_day"),
        @Index(name = "idx_attendance_intern_day", columnList = "intern_id, work_day", unique = true)
})
public class AttendancePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @Column(name = "intern_id")
    private String internId;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    @Column(name = "work_day")
    @Temporal(TemporalType.DATE)
    private Date workDay;
}
