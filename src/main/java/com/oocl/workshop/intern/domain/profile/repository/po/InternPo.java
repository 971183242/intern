package com.oocl.workshop.intern.domain.profile.repository.po;

import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import lombok.Data;

import javax.persistence.Embedded;
import javax.persistence.Entity;

@Data
@Entity
public class InternPo extends UserPo {

    @Embedded
    private InternPeriod period;
}
