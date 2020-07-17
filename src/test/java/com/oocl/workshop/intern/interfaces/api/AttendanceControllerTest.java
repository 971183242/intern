package com.oocl.workshop.intern.interfaces.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oocl.workshop.intern.app.service.AttendanceAppService;
import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.interfaces.dto.attendance.AttendanceDTO;
import com.oocl.workshop.intern.interfaces.dto.profile.UserDTO;
import com.oocl.workshop.intern.support.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttendanceAppService attendanceAppService;

    @Test
    void should_get_empty_attendance_data_from_attendance_controller() throws Exception {
        when(attendanceAppService.findAttendances(any(), any())).thenReturn(new PeriodAttendance());

        Assertions.assertNotNull(getSearchPeriodResultAction());
    }

    @Test
    void should_get_attendance_data_from_service() throws Exception {
        int attendanceCount = 3;
        String internId = "guda";
        when(attendanceAppService.findAttendances(internId, DateUtil.parseDate("2020-07-08"))).thenReturn(getMockPeriodAttendanceWithNAttendance(internId, attendanceCount));
        List<AttendanceDTO> attendanceDTOs = new ObjectMapper().readerForListOf(AttendanceDTO.class).readValue(getSearchPeriodResultAction());

        assertEquals(attendanceCount, attendanceDTOs.size());
        attendanceDTOs.forEach(dto -> assertEquals(internId, dto.getInternId()));
    }

    @Test
    void should_check_in_successfully() throws Exception {
        when(attendanceAppService.checkIn(any(), any())).thenReturn(new DailyAttendance());
        String result = getCheckInResultActions().andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue(Boolean.parseBoolean(result));
    }

    @Test
    void should_cancel_check_in_success() throws Exception {
        Mockito.doNothing().when(attendanceAppService).cancelCheckIn(anyLong());

        AttendanceDTO dto = attendanceDtoSupplier().get();
        String result = mockMvc.perform(post("/attendance/cancelCheckIn")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(Boolean.parseBoolean(result));
    }

    @Test
    void should_confirm_attendance_success() throws Exception {
        List<AttendanceDTO> dtos = Stream.generate(this::attendanceDtoSupplier).map(Supplier::get).limit(3).collect(toList());
        when(attendanceAppService.confirmPeriodAttendance(any())).thenReturn(new PeriodAttendance());
        String result = mockMvc.perform(post("/attendance/confirm")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dtos)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(Boolean.parseBoolean(result));
    }

    @Test
    void should_get_team_interns_success() throws Exception {
        when(attendanceAppService.getInternsActiveInDateContainedPeriod(any(), any())).thenReturn(Collections.emptyList());
        String result = mockMvc.perform(get("/attendance/getInterns").param("teamId", "fwk").param("date", "2020-07-17"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<UserDTO> interns = new ObjectMapper().readerForListOf(UserDTO.class).readValue(result);
        assertEquals(0, interns.size());
    }

    private PeriodAttendance getMockPeriodAttendanceWithNAttendance(String interId, int attendanceCount) {
        PeriodAttendance periodAttendance = new PeriodAttendance();
        periodAttendance.getAttendances().addAll(Stream.generate(() -> this.dailyAttendanceSupplier(interId))
                .map(Supplier::get)
                .limit(attendanceCount)
                .collect(toList()));
        return periodAttendance;
    }

    private Supplier<AttendanceDTO> attendanceDtoSupplier() {
        return () -> {
            AttendanceDTO dto = new AttendanceDTO();
            dto.setAttendanceId(1L);
            dto.setWorkDay(DateUtil.formatDate(new Date()));
            dto.setAttendanceStatus(AttendanceStatus.CheckedIn.name());
            return dto;
        };
    }

    private ResultActions getCheckInResultActions() throws Exception {
        AttendanceDTO dto = attendanceDtoSupplier().get();
        return mockMvc.perform(post("/attendance/checkIn")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)));
    }

    private String getSearchPeriodResultAction() throws Exception {
        return mockMvc.perform(get("/attendance/searchPeriod")
                .param("userId", "guda")
                .param("date", "2020-07-08")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    private Supplier<DailyAttendance> dailyAttendanceSupplier(String interId) {
        return () -> {
            DailyAttendance attendance = new DailyAttendance();
            attendance.setInternId(interId);
            attendance.setAttendanceStatus(AttendanceStatus.CheckedIn);
            attendance.setWorkDay(new Date());
            return attendance;
        };
    }
}