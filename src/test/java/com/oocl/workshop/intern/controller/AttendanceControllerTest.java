package com.oocl.workshop.intern.controller;

import com.oocl.workshop.intern.interfaces.api.AttendanceController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class AttendanceControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AttendanceController()).build();
    }

    @Test
    void should_get_empty_attendance_data_from_attendance_controller() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/attendance/period/{userId}/{date}", "guda", "2020-07-08")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
        Assertions.assertNotNull(result);
    }
}