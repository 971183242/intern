package com.oocl.workshop.intern.app.service.impl;

import com.oocl.workshop.intern.app.service.ProfileAppService;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.DATE;

@Data
@Service
public class ProfileAppServiceImpl  implements ProfileAppService {

    @Autowired
    private ProfileDomService profileDomService;

    @Override
    public List<Intern> getInterns(String teamId, Date date) {
        Calendar currentMonthFirstDate = Calendar.getInstance();
        currentMonthFirstDate.setTime(date);
        currentMonthFirstDate.set(DATE, 1);
        return profileDomService.findTeamInterns(teamId, currentMonthFirstDate.getTime(), date);
    }
}
