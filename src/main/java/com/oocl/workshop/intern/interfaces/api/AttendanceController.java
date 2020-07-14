package com.oocl.workshop.intern.interfaces.api;

import com.oocl.workshop.intern.interfaces.dto.attendance.AttendanceDTO;
import com.oocl.workshop.intern.support.common.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @GetMapping("/period/{userId}/{date}")
    public AttendanceDTO getPeriodAttendance(@PathVariable String userId, @PathVariable String date) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCodes.ATTENDANCE_RECORD_NOT_FOUND);
//        return new AttendanceDTO();
    }
}
