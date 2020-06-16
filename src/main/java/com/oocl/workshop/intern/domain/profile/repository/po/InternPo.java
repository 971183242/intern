package com.oocl.workshop.intern.domain.profile.repository.po;

import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Intern")
public class InternPo extends BaseUserPo {

    @Embedded
    private InternPeriod period;

    public InternPeriod getPeriod() {
        return period;
    }

    public void setPeriod(InternPeriod period) {
        this.period = period;
    }
}
