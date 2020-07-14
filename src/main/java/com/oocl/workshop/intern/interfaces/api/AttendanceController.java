package com.oocl.workshop.intern.interfaces.api;

import com.oocl.workshop.intern.app.service.AttendanceAppService;
import com.oocl.workshop.intern.interfaces.assembler.AttendanceAssembler;
import com.oocl.workshop.intern.interfaces.dto.attendance.AttendanceDTO;
import com.oocl.workshop.intern.support.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

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

    @GetMapping(value = "/searchPeriod", produces = APPLICATION_JSON_VALUE)
    public List<AttendanceDTO> getPeriodAttendance(@RequestParam("userId") String userId, @RequestParam("date") String date) throws ParseException {
        return attendanceAppService.findAttendances(userId, DateUtil.parseDate(date)).getAttendances().stream()
                .map(AttendanceAssembler::toDTO)
                .collect(toList());
    }

    @PostMapping(value = "/checkIn", produces = APPLICATION_JSON_VALUE)
    public boolean internCheckIn(@RequestBody AttendanceDTO dto) throws ParseException {
        return Objects.nonNull(attendanceAppService.checkIn(dto.getInternId(), DateUtil.parseDate(dto.getWorkDay())));
    }

    @PostMapping(value = "/cancelCheckIn", produces = APPLICATION_JSON_VALUE)
    public boolean internCancelCheckIn(@RequestBody AttendanceDTO dto) {
        attendanceAppService.cancelCheckIn(dto.getAttendanceId());
        return true;
    }

    @PostMapping(value = "/confirm", produces = APPLICATION_JSON_VALUE)
    public boolean confirmAttendance(@RequestBody List<AttendanceDTO> dtos) {
        return dtos.stream().map(AttendanceAssembler::toDO).map(attendanceAppService::confirm).allMatch(Objects::nonNull);
    }
}
