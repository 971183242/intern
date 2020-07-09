package com.oocl.workshop.intern.domain.attendance.service;

import com.google.gson.Gson;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.repostitory.po.AttendancePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;

@Service
public class AttendanceFactory {

    static Logger logger = LoggerFactory.getLogger(AttendanceFactory.class);

    public AttendancePo createPo(DailyAttendance attendance) {
        logger.info("create attendance:" + new Gson().toJson(attendance));
        AttendancePo attendancePo = new AttendancePo();
        attendancePo.setAttendanceId(attendance.getAttendanceId());
        updatePo(attendance, attendancePo);
        return attendancePo;
    }

    public void updatePo(DailyAttendance attendance, AttendancePo attendancePo) {
        attendancePo.setAttendanceStatus(attendance.getAttendanceStatus());
        attendancePo.setInternId(attendance.getInternId());
        attendancePo.setWorkDay(attendance.getWorkDay());
        attendancePo.setVersion(attendance.getVersion());
    }

    public DailyAttendance getAttendance(AttendancePo attendancePo) {
        logger.info("getAttendance attendance. Po:" + new Gson().toJson(attendancePo));
        DailyAttendance attendance = new DailyAttendance();
        attendance.setAttendanceId(attendancePo.getAttendanceId());
        attendance.setInternId(attendancePo.getInternId());
        attendance.setWorkDay(attendancePo.getWorkDay());
        attendance.setAttendanceStatus(attendancePo.getAttendanceStatus());
        attendance.setVersion(attendancePo.getVersion());
        logger.info("getAttendance DailyAttendance:" + new Gson().toJson(attendance));
        return attendance;
    }

}
