package com.oocl.workshop.intern.app.service.impl;

import com.oocl.workshop.intern.app.service.AttendanceAppService;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceDomService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus.CheckedIn;

@Data
@Service
public class AttendanceAppServiceImpl implements AttendanceAppService {

    @Autowired
    private AttendanceDomService attendanceDomService;


    @Override
    public DailyAttendance checkIn(String internId, Date date) {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setInternId(internId);
        attendance.setWorkDay(date);
        attendance.setAttendanceStatus(CheckedIn);
        return attendanceDomService.createAttendance(attendance);
    }

    @Override
    public DailyAttendance cancelCheckIn(long id) {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setAttendanceId(id);
        return attendanceDomService.removeAttendance(attendance);
    }
}
