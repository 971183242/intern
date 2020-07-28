package com.demo.workshop.intern.domain.profile.entity;

import com.demo.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import lombok.Data;

@Data
public class Intern extends User {

    private InternPeriod period;

    private Team team;
}
