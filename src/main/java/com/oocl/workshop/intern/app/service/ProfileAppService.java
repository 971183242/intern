package com.oocl.workshop.intern.app.service;

import com.oocl.workshop.intern.domain.profile.entity.Intern;

import java.util.Date;
import java.util.List;

public interface ProfileAppService {
    List<Intern> getInterns(String team, Date parseDate);
}
