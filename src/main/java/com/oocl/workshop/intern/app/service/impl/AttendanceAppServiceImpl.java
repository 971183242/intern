package com.oocl.workshop.intern.app.service.impl;

import com.oocl.workshop.intern.app.service.AttendanceAppService;
import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.facade.AttendanceRepo;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceDomService;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomService;
import com.oocl.workshop.intern.domain.report.service.MonthlySettlementDayRuleService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@Service
public class AttendanceAppServiceImpl implements AttendanceAppService {

    @Autowired
    private AttendanceDomService attendanceDomService;

    private AttendanceRepo attendanceRepo;

    private ProfileDomService profileDomService;

    @Autowired
    public void setProfileDomService(ProfileDomService profileDomService) {
        this.profileDomService = profileDomService;
    }

    @Autowired
    public void setAttendanceRepo(AttendanceRepo attendanceRepo) {
        this.attendanceRepo = attendanceRepo;
    }

    private MonthlySettlementDayRuleService monthlySettlementDayRuleService;

    @Autowired
    public void setMonthlySettlementDayRuleService(MonthlySettlementDayRuleService monthlySettlementDayRuleService) {
        this.monthlySettlementDayRuleService = monthlySettlementDayRuleService;
    }

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public DailyAttendance checkIn(String internId, Date date) {
        return attendanceDomService.createAttendance(internId, date);
    }

    @Override
    public void cancelCheckIn(long id) {
        attendanceDomService.removeAttendance(id);
    }

    @Override
    public boolean containsUnconfirmedAttendance(String internId) {
        return !attendanceRepo.findByInternIdAndAttendanceStatus(internId, AttendanceStatus.CheckedIn).isEmpty();
    }

    @Override
    public PeriodAttendance findAttendances(String internId, Date date) {
        List<Date> timeWindow = monthlySettlementDayRuleService.getMonthlySettlementDateWindow(date);
        return attendanceDomService.getPeriodAttendance(internId, timeWindow.get(0), timeWindow.get(1));
    }

    @Override
    public List<Intern> getInternsActiveInDateContainedPeriod(String teamId, Date date) {
        Calendar currentMonthFirstDate = Calendar.getInstance();
        List<Date> settlementDateWindow = monthlySettlementDayRuleService.getMonthlySettlementDateWindow(date);
        return profileDomService.findTeamInterns(teamId, settlementDateWindow.get(0), settlementDateWindow.get(1));
    }

    @Override
    public PeriodAttendance confirmPeriodAttendance(PeriodAttendance periodAttendance) {
        return attendanceDomService.confirmPeriodAttendance(periodAttendance);
    }
}
