package com.oocl.workshop.intern.domain.profile.entity.valueobject;

import lombok.Data;

import javax.persistence.Embeddable;
import java.util.Date;

@Data
@Embeddable
public class InternPeriod {
    private Date dateFrom;
    private Date dateTo;
}
