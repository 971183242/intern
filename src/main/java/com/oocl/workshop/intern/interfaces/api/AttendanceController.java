package com.oocl.workshop.intern.interfaces.api;

import com.oocl.workshop.intern.interfaces.dto.attendance.AttendanceDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @GetMapping("/period/{userId}/{date}")
    public AttendanceDTO getPeriodAttendance(@PathVariable String userId, @PathVariable String date) {
        return new AttendanceDTO();
    }
}
