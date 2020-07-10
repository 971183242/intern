package com.oocl.workshop.intern.interfaces.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oocl.workshop.intern.app.service.AttendanceAppService;
import com.oocl.workshop.intern.domain.attendance.entity.AttendanceStatus;
import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.interfaces.dto.attendance.AttendanceDTO;
import com.oocl.workshop.intern.support.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AttendanceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AttendanceAppService attendanceAppService;

    @InjectMocks
    AttendanceController attendanceController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(attendanceController).build();
        MockitoAnnotations.initMocks(attendanceAppService);
    }

    @Test
    void should_get_empty_attendance_data_from_attendance_controller() throws Exception {
        when(attendanceAppService.findAttendances(any(), any())).thenReturn(new PeriodAttendance());
        String result = getSearchPeriodResultAction()
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertNotNull(result);
    }

    @Test
    void should_get_attendance_data_from_service() throws Exception {
        int attendanceCount = 3;
        String interId = "guda";
        PeriodAttendance periodAttendance = getMockPeriodAttendanceWithNAttendance(interId, attendanceCount);

        when(attendanceAppService.findAttendances(interId, DateUtil.parseDate("2020-07-08"))).thenReturn(periodAttendance);
        String result = getSearchPeriodResultAction()
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<AttendanceDTO> attendanceDTOs = new ObjectMapper().readerForListOf(AttendanceDTO.class).readValue(result);
        assertEquals(attendanceCount, attendanceDTOs.size());
        attendanceDTOs.forEach(dto -> assertEquals(interId, dto.getInternId()));
    }

    private PeriodAttendance getMockPeriodAttendanceWithNAttendance(String interId, int attendanceCount) {
        PeriodAttendance periodAttendance = new PeriodAttendance();
        for (int i = 0; i < attendanceCount; i++) {
            DailyAttendance attendance = new DailyAttendance();
            attendance.setInternId(interId);
            attendance.setAttendanceStatus(AttendanceStatus.CheckedIn);
            attendance.setWorkDay(new Date());
            periodAttendance.getAttendances().add(attendance);
        }
        return periodAttendance;
    }

    private ResultActions getSearchPeriodResultAction() throws Exception {
        return mockMvc.perform(get("/attendance/searchPeriod")
                .param("userId", "guda")
                .param("date", "2020-07-08")
                .accept(MediaType.APPLICATION_JSON));
    }
}