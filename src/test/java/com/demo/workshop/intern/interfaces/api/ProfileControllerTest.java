package com.demo.workshop.intern.interfaces.api;

import com.demo.workshop.intern.app.service.ProfileAppService;
import com.demo.workshop.intern.domain.profile.entity.Employee;
import com.demo.workshop.intern.domain.profile.entity.Intern;
import com.demo.workshop.intern.domain.profile.entity.Role;
import com.demo.workshop.intern.domain.profile.entity.Team;
import com.demo.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.demo.workshop.intern.configuration.CommonAuthenticationProvider;
import com.demo.workshop.intern.configuration.MyAuthenticationFailHandler;
import com.demo.workshop.intern.configuration.MyAuthenticationSuccessHandler;
import com.demo.workshop.intern.interfaces.dto.profile.InternDTO;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProfileController.class)
@Import(value = {MyAuthenticationFailHandler.class, MyAuthenticationSuccessHandler.class, CommonAuthenticationProvider.class})
class ProfileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProfileAppService profileAppService;

    @Test
    void findAllTeams() throws Exception {
        Team team = new Team();
        team.setTeamId("TMS");
        team.setName("Tms");

        Employee employee = new Employee();
        employee.setDomainId("leader001");
        team.setTeamLeader(employee);
        given(profileAppService.findAllTeams()).willReturn(Lists.newArrayList(team));

        this.mockMvc.perform(get("/profile/teams"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].teamId").value("TMS"))
                .andExpect(jsonPath("$[0].name").value("Tms"))
                .andExpect(jsonPath("$[0].teamLeader.domainId").value("leader001"))
                .andReturn();
    }

    @Test
    void findUser() throws Exception {
        Team team = new Team();
        team.setTeamId("tms");
        Intern intern = new Intern();
        intern.setDomainId("INTERN007");
        intern.setName("INTERN_007");
        intern.setEmail("intern007@oocl.com");
        intern.setRoles(Lists.newArrayList(Role.INTERN));
        intern.setPeriod(new InternPeriod(new Date(), new Date()));
        intern.setTeam(team);
        given(profileAppService.findUserByDomainId("INTERN007")).willReturn(Optional.of(intern));

        this.mockMvc.perform(get("/profile/user/{domainId}", "INTERN007"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.domainId").value(intern.getDomainId()))
                .andExpect(jsonPath("$.name").value(intern.getName()))
                .andExpect(jsonPath("$.email").value(intern.getEmail()))
                .andExpect(jsonPath("$.roles[0]").value(Role.INTERN.getFullName()))
                .andExpect(jsonPath("$.team.teamId").value(team.getTeamId()))
                .andExpect(jsonPath("$.internPeriodFromDate").value(MatchesPattern.matchesPattern("\\d{4}-\\d{2}-\\d{2}")))
                .andExpect(jsonPath("$.internPeriodToDate").value(MatchesPattern.matchesPattern("\\d{4}-\\d{2}-\\d{2}")));
    }

    @Test
    void createIntern() throws Exception {
        InternDTO param = new InternDTO();
        param.setDomainId("intern007");
        param.setName("intern007");
        param.setEmail("test@oocl.com");

        Intern intern = new Intern();
        intern.setDomainId(param.getDomainId());
        intern.setName(param.getName());
        intern.setEmail(param.getEmail());
        when(profileAppService.createUser(any())).thenReturn(intern);

        this.mockMvc.perform(post("/profile/createIntern")
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(param.getName()))
                .andExpect(jsonPath("$.domainId").value(param.getDomainId()))
                .andExpect(jsonPath("$.email").value(param.getEmail()));

        verify(profileAppService).createUser(
                argThat(s -> s.getDomainId().equals(param.getDomainId())
                        && s.getName().equals(param.getName())
                        && s.getEmail().equals(param.getEmail())));
    }

    @Test
    void updateIntern() throws Exception {
        InternDTO param = new InternDTO();
        param.setDomainId("intern007");
        param.setName("intern007");
        param.setEmail("test@oocl.com");

        Intern intern = new Intern();
        intern.setDomainId(param.getDomainId());
        intern.setName(param.getName());
        intern.setEmail(param.getEmail());
        when(profileAppService.updateUser(any())).thenReturn(intern);

        this.mockMvc.perform(post("/profile/updateIntern")
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(param.getName()))
                .andExpect(jsonPath("$.domainId").value(param.getDomainId()))
                .andExpect(jsonPath("$.email").value(param.getEmail()));

        verify(profileAppService).updateUser(
                argThat(s -> s.getDomainId().equals(param.getDomainId())
                        && s.getName().equals(param.getName())
                        && s.getEmail().equals(param.getEmail())));
    }

    @Test
    void getInterns() throws Exception {
        Intern intern = new Intern();
        intern.setDomainId("intern007");
        intern.setName("INTERN007");
        intern.setEmail("intern007@oocl.com");
        intern.setPeriod(new InternPeriod(new Date(), new Date()));
        intern.setTeam(new Team());
        given(profileAppService.getInterns(any())).willReturn(Lists.newArrayList(intern));

        this.mockMvc.perform(get("/profile/getInterns").param("date", "2020-07-26"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].domainId").value(intern.getDomainId()))
                .andExpect(jsonPath("$[0].name").value(intern.getName()))
                .andExpect(jsonPath("$[0].email").value(intern.getEmail()));

        verify(profileAppService).getInterns(any(Date.class));
    }
}