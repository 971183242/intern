package com.oocl.workshop.intern.domain.intern.repostitory.po;

import com.oocl.workshop.intern.domain.intern.entity.AttendaceStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class Attendance {

    @Id
    private Long id;

    private Long internId;

    @Enumerated(EnumType.STRING)
    private AttendaceStatus attendaceStatus;

    @Temporal(TemporalType.DATE)
    private Date workDay;
}
