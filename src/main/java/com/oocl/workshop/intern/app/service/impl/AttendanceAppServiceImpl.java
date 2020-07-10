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
import com.oocl.workshop.intern.support.InternApplicationException;
import com.oocl.workshop.intern.support.common.ErrorCodes;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public DailyAttendance checkIn(String internId, Date date) {
        return attendanceDomService.createAttendance(internId, date);
    }

    @Override
    public void cancelCheckIn(long id) {
        attendanceDomService.removeAttendance(id);
    }

    /**
     * Partially update with DailyAttendance
     * Now no concurrency check, to be supplemented
     * @param requestAttendance only use attendanceStatus
     * @return
     */
    @Override
    public DailyAttendance confirm(DailyAttendance requestAttendance) {
        DailyAttendance dailyAttendance = attendanceDomService.getAttendance(requestAttendance.getAttendanceId());
        if (dailyAttendance == null) {
            throw new InternApplicationException(ErrorCodes.ATTENDANCE_RECORD_NOT_FOUND);
        }
        if (AttendanceStatus.Approved.equals(requestAttendance.getAttendanceStatus())) {
            approveAttendance(dailyAttendance);
        } else if (AttendanceStatus.Rejected.equals(requestAttendance.getAttendanceStatus())) {
            rejectAttendance(dailyAttendance);
        } else {
            throw new InternApplicationException(ErrorCodes.INVALID_ATTENDANCE_CONFIRM_STATUS);
        }
        return attendanceDomService.updateAttendance(dailyAttendance);
    }

    private void rejectAttendance(DailyAttendance dailyAttendance) {
        if (AttendanceStatus.Approved.equals(dailyAttendance.getAttendanceStatus())) {
            throw new InternApplicationException(ErrorCodes.TRY_TO_REJECT_APPROVED_ATTENDANCE);
        }
        dailyAttendance.setAttendanceStatus(AttendanceStatus.Rejected);
    }

    private void approveAttendance(DailyAttendance dailyAttendance) {
        dailyAttendance.setAttendanceStatus(AttendanceStatus.Approved);
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
}
