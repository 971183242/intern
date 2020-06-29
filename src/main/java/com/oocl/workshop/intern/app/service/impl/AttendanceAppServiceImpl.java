package com.oocl.workshop.intern.app.service.impl;

import com.oocl.workshop.intern.app.service.AttendanceAppService;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceDomService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Data
@Service
public class AttendanceAppServiceImpl implements AttendanceAppService {

    @Autowired
    private AttendanceDomService attendanceDomService;


    @Override
    public DailyAttendance checkIn(String internId, Date date) {
        return attendanceDomService.createAttendance(internId, date);
    }

    @Override
    public void cancelCheckIn(long id) {
        attendanceDomService.removeAttendance(id);
    }
}
