package com.oocl.workshop.intern.domain.attendance.service.impl;

import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.facade.AttendanceRepo;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceFactory;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceDomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceDomServiceImpl implements AttendanceDomService {

    @Autowired
    private AttendanceFactory attendanceFactory;

    @Autowired
    private AttendanceRepo attendanceRepo;

    private DailyAttendance createAttendance(DailyAttendance attendance) {
        AttendancePo attendancePo = attendanceFactory.createPo(attendance);
        attendancePo = attendanceRepo.save(attendancePo);
        return attendanceFactory.getAttendance(attendancePo);
    }

    @Override
    public DailyAttendance createAttendance(String internId, Date workDay) {
        DailyAttendance dailyAttendance = new DailyAttendance();
        dailyAttendance.setAttendanceStatus(AttendanceStatus.CheckedIn);
        dailyAttendance.setInternId(internId);
        dailyAttendance.setWorkDay(workDay);
        return createAttendance(dailyAttendance);
    }

    @Override
    public void removeAttendance(long attendanceId) {
        attendanceRepo.deleteById(attendanceId);
    }

    @Override
    public PeriodAttendance confirmPeriodAttendance(PeriodAttendance periodAttendance) {
        List<DailyAttendance> dailyAttendanceList = periodAttendance.getAttendances().stream()
                .map(this::confirmAttendance)
                .collect(Collectors.toList());
        periodAttendance.setAttendances(dailyAttendanceList);
        return periodAttendance;
    }

    @Override
    public List<DailyAttendance> findByInternIdAndStatus(String internId, AttendanceStatus status) {
        List<AttendancePo> attendancePoList = attendanceRepo.findByInternIdAndStatus(internId, status);
        return attendancePoList.stream().map(po -> attendanceFactory.getAttendance(po)).collect(Collectors.toList());
    }

    @Override
    public PeriodAttendance findByInternIdAndBetweenDate(String internId, Date startDate, Date endDate) {
        List<AttendancePo> attendancePoList = attendanceRepo.findByInternIdAndWorkDayBetweenOrderByWorkDay(internId, startDate, endDate);
        PeriodAttendance periodAttendance = new PeriodAttendance();
        periodAttendance.setAttendances(attendancePoList.stream().map(po -> attendanceFactory.getAttendance(po)).collect(Collectors.toList()));
        periodAttendance.setInternId(internId);
        periodAttendance.setStartDate(startDate);
        periodAttendance.setEndDate(endDate);
        return null;
    }

    private DailyAttendance confirmAttendance(DailyAttendance attendance) {
        DailyAttendance result;
        switch (attendance.getAttendanceStatus()) {
            case Approved:
                result = approveAttendance(attendance);
                break;
            case Rejected:
                result = rejectAttendance(attendance);
                break;
            default:
                result = attendance;
                break;
        }
        return result;
    }

    private DailyAttendance rejectAttendance(DailyAttendance attendance) {
        AttendancePo attendancePo = attendanceFactory.createPo(attendance);
        attendancePo.setAttendanceStatus(AttendanceStatus.Rejected);
        attendancePo = attendanceRepo.save(attendancePo);
        return attendanceFactory.getAttendance(attendancePo);
    }

    private DailyAttendance approveAttendance(DailyAttendance attendance) {
        AttendancePo attendancePo = attendanceFactory.createPo(attendance);
        attendancePo.setAttendanceStatus(AttendanceStatus.Approved);
        attendancePo = attendanceRepo.save(attendancePo);
        return attendanceFactory.getAttendance(attendancePo);
    }

}
