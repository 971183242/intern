package com.oocl.workshop.intern.interfaces.api;

import com.oocl.workshop.intern.app.service.ProfileAppService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileAppService profileAppService;

    @Test
    void findAllTeams() {
    }

    @Test
    void findUser() {
    }

    @Test
    void createUser() {
    }

    @Test
    void updateUser() {
    }
}