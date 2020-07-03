package com.oocl.workshop.intern.app.service;

import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.domain.profile.entity.Intern;

import java.util.Date;
import java.util.List;

public interface AttendanceAppService {
    DailyAttendance checkIn(String internId, Date date);

    void cancelCheckIn(long id);

    DailyAttendance confirm(DailyAttendance requestAttendance);

    boolean containsUnconfirmedAttendance(String internId);

    PeriodAttendance findAttendances(String internId, Date date);

    List<Intern> getInternsActiveInDateContainedPeriod(String teamId, Date date);
}
