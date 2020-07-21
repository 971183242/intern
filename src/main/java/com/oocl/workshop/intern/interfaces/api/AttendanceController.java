package com.oocl.workshop.intern.interfaces.api;

import com.oocl.workshop.intern.app.service.AttendanceAppService;
import com.oocl.workshop.intern.app.service.ProfileAppService;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.interfaces.assembler.AttendanceAssembler;
import com.oocl.workshop.intern.interfaces.assembler.ProfileAssembler;
import com.oocl.workshop.intern.interfaces.dto.ResultDto;
import com.oocl.workshop.intern.interfaces.dto.attendance.AttendanceDTO;
import com.oocl.workshop.intern.interfaces.dto.profile.UserDTO;
import com.oocl.workshop.intern.support.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final ProfileAppService profileAppService;

    private AttendanceAppService attendanceAppService;

    @Autowired
    public AttendanceController(ProfileAppService profileAppService) {
        this.profileAppService = profileAppService;
    }

    @Autowired
    public void setAttendanceAppService(AttendanceAppService attendanceAppService) {
        this.attendanceAppService = attendanceAppService;
    }

    @GetMapping(value = "/searchPeriod", produces = APPLICATION_JSON_VALUE)
    public ResultDto getPeriodAttendance(@RequestParam("userId") String userId, @RequestParam("date") String date) throws ParseException {
        return ResultDto.success(attendanceAppService.findAttendances(userId, DateUtil.parseDate(date)).getAttendances().stream()
                .map(AttendanceAssembler::toDTO)
                .collect(toList()));
    }

    @PostMapping(value = "/checkIn", consumes = APPLICATION_JSON_VALUE)
    public ResultDto internCheckIn(@RequestBody AttendanceDTO dto) throws ParseException {
        return ResultDto.success(attendanceAppService.checkIn(dto.getInternId(), DateUtil.parseDate(dto.getWorkDay())));
    }

    @PostMapping(value = "/cancelCheckIn", consumes = APPLICATION_JSON_VALUE)
    public ResultDto internCancelCheckIn(@RequestBody AttendanceDTO dto) {
        attendanceAppService.cancelCheckIn(dto.getAttendanceId());
        return ResultDto.success();
    }

    @PostMapping(value = "/confirm", consumes = APPLICATION_JSON_VALUE)
    public boolean confirmAttendance(@RequestBody List<AttendanceDTO> dtos) {
        PeriodAttendance periodAttendance = new PeriodAttendance();
        dtos.stream().map(AttendanceAssembler::toDO).forEach(periodAttendance.getAttendances()::add);
        attendanceAppService.confirmPeriodAttendance(periodAttendance);
        return true;
    }

    @GetMapping(value = "/getInterns", produces = APPLICATION_JSON_VALUE)
    public ResultDto getTeamInterns(@RequestParam("teamId") String teamId, @RequestParam("date") String dateStr) throws ParseException {
        List<Intern> interns;
        if ("all".equals(teamId)) {
            interns = profileAppService.getInterns(DateUtil.parseDate(dateStr));
        } else {
            interns = attendanceAppService.getInternsActiveInDateContainedPeriod(teamId, DateUtil.parseDate(dateStr));
        }
        List<UserDTO> userDTOS = interns.stream().map(ProfileAssembler::toDTO).collect(toList());
        return ResultDto.success(userDTOS);
    }
}
