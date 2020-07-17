package com.oocl.workshop.intern.interfaces.assembler;

import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.interfaces.dto.attendance.AttendanceDTO;
import com.oocl.workshop.intern.support.util.DateUtil;
import org.springframework.beans.BeanUtils;

public class AttendanceAssembler {
    public static AttendanceDTO toDTO(DailyAttendance attendance) {
        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setAttendanceId(attendance.getAttendanceId());
        attendanceDTO.setInternId(attendance.getInternId());
        attendanceDTO.setAttendanceStatus(attendance.getAttendanceStatus().name());
        attendanceDTO.setWorkDay(DateUtil.formatDate(attendance.getWorkDay()));
        attendanceDTO.setVersion(attendance.getVersion());
        return attendanceDTO;
    }

    public static DailyAttendance toDO(AttendanceDTO dto) {
        DailyAttendance dailyAttendance = new DailyAttendance();
        BeanUtils.copyProperties(dto, dailyAttendance);
        dailyAttendance.setAttendanceStatus(AttendanceStatus.valueOf(dto.getAttendanceStatus()));
        return dailyAttendance;
    }
}
