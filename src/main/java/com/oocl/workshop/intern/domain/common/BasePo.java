package com.oocl.workshop.intern.domain.common;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class BasePo {
    @CreatedBy
    private String createdBy;

    @CreatedDate
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    @Temporal(TemporalType.DATE)
    private Date lastModifiedDate;

    @Version
    private int version;
}
