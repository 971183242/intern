package com.oocl.workshop.intern.domain.attendance.service.impl;

import com.google.gson.Gson;
import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.facade.AttendanceRepo;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceDomService;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceDomServiceImpl implements AttendanceDomService {

    static Logger logger = LoggerFactory.getLogger(AttendanceDomServiceImpl.class);

    @Autowired
    private AttendanceFactory attendanceFactory;

    @Autowired
    private AttendanceRepo attendanceRepo;

    private DailyAttendance createAttendance(DailyAttendance attendance) {
        AttendancePo attendancePo = attendanceFactory.createPo(attendance);
        attendancePo = attendanceRepo.save(attendancePo);
        logger.info("createAttendance Po:" + new Gson().toJson(attendancePo));
        return attendanceFactory.getAttendance(attendancePo);
    }

    @Override
    public DailyAttendance createAttendance(String internId, Date workDay) {
        logger.info(String.format("createAttendance. internId:%s, workDay:%s", internId, workDay));
        DailyAttendance dailyAttendance = new DailyAttendance();
        dailyAttendance.setAttendanceStatus(AttendanceStatus.CheckedIn);
        dailyAttendance.setInternId(internId);
        dailyAttendance.setWorkDay(workDay);
        return createAttendance(dailyAttendance);
    }

    @Override
    public void removeAttendance(long attendanceId) {
        logger.info(String.format("removeAttendance. attendanceId:%s", attendanceId));
        attendanceRepo.deleteById(attendanceId);
    }

    @Override
    public PeriodAttendance confirmPeriodAttendance(PeriodAttendance periodAttendance) {
        logger.info(String.format("confirmPeriodAttendance. periodAttendance:%s", new Gson().toJson(periodAttendance)));
        List<DailyAttendance> dailyAttendanceList = periodAttendance.getAttendances().stream()
                .map(this::confirmAttendance)
                .collect(Collectors.toList());
        periodAttendance.setAttendances(dailyAttendanceList);
        logger.info(String.format("confirmedPeriodAttendance:%s", new Gson().toJson(periodAttendance)));
        return periodAttendance;
    }

    @Override
    public DailyAttendance getAttendance(long attendanceId) {
        logger.info(String.format("getAttendance. attendanceId:%s", attendanceId));
        return attendanceRepo.findById(attendanceId).map(attendanceFactory::getAttendance).orElse(null);
    }

    @Override
    public DailyAttendance updateAttendance(DailyAttendance dailyAttendance) {
        logger.info(String.format("updateAttendance. dailyAttendance:%s", new Gson().toJson(dailyAttendance)));
        Optional<AttendancePo> attendancePo = attendanceRepo.findById(dailyAttendance.getAttendanceId());
        if (!attendancePo.isPresent()) {
            return createAttendance(dailyAttendance);
        }
        attendanceFactory.updatePo(dailyAttendance, attendancePo.get());
        return attendanceFactory.getAttendance(attendancePo.get());
    }

    @Override
    public PeriodAttendance getPeriodAttendance(String internId, Date startDate, Date endDate) {
        logger.info(String.format("getPeriodAttendance. internId:%s, startDate:%s, endDate:%s", internId, startDate, endDate));
        List<AttendancePo> attendancePos = attendanceRepo.findByInternIdAndWorkDayBetweenOrderByWorkDay(internId, startDate, endDate);
        PeriodAttendance periodAttendance = new PeriodAttendance();
        periodAttendance.setInternId(internId);
        periodAttendance.setStartDate(startDate);
        periodAttendance.setEndDate(endDate);
        periodAttendance.getAttendances().addAll(attendancePos.stream().filter(Objects::nonNull).map(attendanceFactory::getAttendance).collect(Collectors.toList()));
        periodAttendance.setApprovedAttendanceCount(getCountByStatus(attendancePos, AttendanceStatus.Approved));
        periodAttendance.setRejectedAttendanceCount(getCountByStatus(attendancePos, AttendanceStatus.Rejected));
        periodAttendance.setCheckedInAttendanceCount(getCountByStatus(attendancePos, AttendanceStatus.CheckedIn));
        return periodAttendance;
    }

    private int getCountByStatus(List<AttendancePo> attendancePos, AttendanceStatus status) {
        return (int) attendancePos.stream().map(AttendancePo::getAttendanceStatus).filter(status::equals).count();
    }

    @Override
    public List<DailyAttendance> findByInternIdAndStatus(String internId, AttendanceStatus status) {
        logger.info(String.format("findByInternIdAndStatus. internId:%s, attendanceStatus:%s", internId, status));
        List<AttendancePo> attendancePoList = attendanceRepo.findByInternIdAndAttendanceStatus(internId, status);
        return attendancePoList.stream().map(po -> attendanceFactory.getAttendance(po)).collect(Collectors.toList());
    }

    @Override
    public DailyAttendance findByInternIdAndWorkDay(String internId, Date workDay) {
        logger.info(String.format("findByInternIdAndWorkDay. internId:%s, attendanceStatus:%s", internId, workDay.toString()));
        List<AttendancePo> specificDailyAttendancePo = attendanceRepo.findByInternIdAndWorkDayBetweenOrderByWorkDay(internId, workDay, workDay);
        return CollectionUtils.isEmpty(specificDailyAttendancePo) ? null : attendanceFactory.getAttendance(specificDailyAttendancePo.get(0));
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
