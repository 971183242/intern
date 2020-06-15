package com.oocl.workshop.intern.domain.intern.repostitory.po;

import com.oocl.workshop.intern.domain.intern.entity.AttendaceStatus;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Attendance {

    @Id
    private Long id;

    private Long internId;

    private AttendaceStatus attendaceStatus;

    private Date workDay;
}
