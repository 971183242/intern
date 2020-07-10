package com.oocl.workshop.intern.interfaces.api;

import com.oocl.workshop.intern.app.service.AttendanceAppService;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.interfaces.assembler.AttendanceAssembler;
import com.oocl.workshop.intern.interfaces.dto.attendance.AttendanceDTO;
import com.oocl.workshop.intern.support.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private AttendanceAppService attendanceAppService;

    @Autowired
    public void setAttendanceAppService(AttendanceAppService attendanceAppService) {
        this.attendanceAppService = attendanceAppService;
    }

    @GetMapping("/searchPeriod")
    @Procedure(APPLICATION_JSON_VALUE)
    public List<AttendanceDTO> getPeriodAttendance(@RequestParam("userId") String userId, @RequestParam("date") String date) throws ParseException {
        PeriodAttendance attendances = attendanceAppService.findAttendances(userId, DateUtil.parseDate(date));
        return attendances.getAttendances().stream().map(AttendanceAssembler::toDTO).collect(toList());
    }
}
