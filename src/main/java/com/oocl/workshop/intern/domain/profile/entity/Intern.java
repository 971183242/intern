package com.oocl.workshop.intern.domain.profile.entity;

import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import lombok.Data;

@Data
public class Intern extends User {

    private InternPeriod period;

    private Team team;
}
