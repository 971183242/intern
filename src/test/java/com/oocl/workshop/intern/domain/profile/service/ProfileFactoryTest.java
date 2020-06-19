package com.oocl.workshop.intern.domain.profile.service;

import com.oocl.workshop.intern.domain.profile.entity.*;
import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import com.oocl.workshop.intern.domain.profile.repository.facade.TeamRepo;
import com.oocl.workshop.intern.domain.profile.repository.facade.UserRepo;
import com.oocl.workshop.intern.domain.profile.repository.po.TeamPo;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(propagation = Propagation.NESTED)
@TestPropertySource("classpath:application-test.properties")
class ProfileFactoryTest {

    @Autowired
    UserRepo userRepo;
    @Autowired
    TeamRepo teamRepo;
    @Autowired
    ProfileFactory profileFactory;

    @BeforeEach
    void setUp() {
        // 已指定leader的Team
        TeamPo teamPo01 = new TeamPo();
        teamPo01.setTeamId("team_01");
        teamPo01.setName("team_name_01");
        teamPo01.setTeamLeaderId("teamLeader_01");
        teamRepo.save(teamPo01);

        // 未指定leader的Team
        TeamPo teamPo02 = new TeamPo();
        teamPo02.setTeamId("team_02");
        teamPo02.setName("team_name_02");
        teamRepo.save(teamPo02);

        // TeamLeader
        UserPo teamLeaderPo = new UserPo();
        teamLeaderPo.setDomainId("teamLeader_01");
        teamLeaderPo.setEmail("teamLeader_01@oocl.com");
        teamLeaderPo.setName("XX项目负责人");
        teamLeaderPo.setUserType(UserType.TeamLeader);
        userRepo.save(teamLeaderPo);

        // Intern - 指定了Team
        UserPo internPo0 = new UserPo();
        internPo0.setDomainId("intern_0");
        internPo0.setEmail("intern_0@oocl.com");
        internPo0.setName("实习生甲");
        internPo0.setUserType(UserType.Intern);
        internPo0.setInternPeriod(new InternPeriod(new Date(120, 0, 1), new Date(120, 5, 1)));
        internPo0.setTeamId("team_01");
        userRepo.save(internPo0);

        // Intern - 未指定了Team
        UserPo internPo1 = new UserPo();
        internPo1.setDomainId("intern_1");
        internPo1.setEmail("intern_1@oocl.com");
        internPo1.setName("实习生乙");
        internPo1.setUserType(UserType.Intern);
        internPo1.setInternPeriod(new InternPeriod(new Date(120, 0, 1), new Date(120, 5, 1)));
        userRepo.save(internPo1);
    }


    @Test
    void createTeamPo() {
        Team team = new Team();
        team.setName("team_name_02");
        team.setTeamId("team_02");
        TeamLeader teamLeader = new TeamLeader();
        teamLeader.setDomainId("leader_02");
        team.setTeamLeader(teamLeader);

        TeamPo teamPo = profileFactory.createTeamPo(team);
        assertEquals("team_02", teamPo.getTeamId());
        assertEquals("team_name_02", teamPo.getName());
        assertEquals("leader_02", teamPo.getTeamLeaderId());

        team.setTeamLeader(null);
        teamPo = profileFactory.createTeamPo(team);
        assertNull(teamPo.getTeamLeaderId());
    }


    @Test
    void getTeam() {
        Team team01 = profileFactory.getTeam(teamRepo.findById("team_01").get());
        assertEquals("team_01", team01.getTeamId());
        assertEquals("team_name_01", team01.getName());
        assertEquals("teamLeader_01", team01.getTeamLeader().getDomainId());
        assertEquals("XX项目负责人", team01.getTeamLeader().getUserName());
        assertEquals("teamLeader_01@oocl.com", team01.getTeamLeader().getEmail());

        Team team02 = profileFactory.getTeam(teamRepo.findById("team_02").get());
        assertEquals("team_02", team02.getTeamId());
        assertEquals("team_name_02", team02.getName());
        assertNull(team02.getTeamLeader());
    }


    @Test
    void createUserPo() {
        Intern intern = new Intern();
        intern.setDomainId("intern_1");
        intern.setUserName("实习生乙");
        intern.setEmail("intern_1@oocl.com");
        InternPeriod period = new InternPeriod();
        period.setDateFrom(new Date(120, 1, 1));
        intern.setPeriod(period);

        UserPo userPo = profileFactory.createPo(intern);
        assertEquals(UserType.Intern, userPo.getUserType());
        assertEquals(intern.getDomainId(), userPo.getDomainId());
        assertEquals(intern.getEmail(), userPo.getEmail());
        assertEquals(intern.getUserName(), userPo.getName());
        assertEquals(intern.getPeriod().getDateFrom(), userPo.getInternPeriod().getDateFrom());

        TeamLeader leader = new TeamLeader();
        leader.setDomainId("leader");
        leader.setEmail("leader@oocl.com");
        leader.setUserName("leader name");
        UserPo leaderPo = profileFactory.createPo(leader);
        assertEquals(leader.getUserName(), leaderPo.getName());
        assertEquals(leader.getDomainId(), leaderPo.getDomainId());
        assertEquals(leader.getEmail(), leaderPo.getEmail());
        assertEquals(UserType.TeamLeader, leaderPo.getUserType());

        HR hr = new HR();
        hr.setDomainId("hr");
        hr.setEmail("hr@oocl.com");
        hr.setUserName("人事经理");
        UserPo hrPo = profileFactory.createPo(hr);
        assertEquals(hr.getUserName(), hrPo.getName());
        assertEquals(hr.getDomainId(), hrPo.getDomainId());
        assertEquals(hr.getEmail(), hrPo.getEmail());
        assertEquals(UserType.HR, hrPo.getUserType());

        SuperAdmin admin = new SuperAdmin();
        admin.setDomainId("leader");
        admin.setEmail("leader@oocl.com");
        admin.setUserName("leader name");
        UserPo adminPo = profileFactory.createPo(admin);
        assertEquals(admin.getUserName(), adminPo.getName());
        assertEquals(admin.getDomainId(), adminPo.getDomainId());
        assertEquals(admin.getEmail(), adminPo.getEmail());
        assertEquals(UserType.SuperAdmin, adminPo.getUserType());
    }

    @Test
    void getUser() {
        UserPo teamLeader = userRepo.findById("teamLeader_01").get();
        User user = profileFactory.getUser(teamLeader);
        assertEquals("XX项目负责人", user.getUserName());
        assertTrue(user instanceof TeamLeader);

        UserPo intern0 = userRepo.findById("intern_0").get();
        Intern intern = (Intern) profileFactory.getUser(intern0);
        assertEquals("实习生甲", intern.getUserName());
        assertEquals(new Date(120, 5, 1), intern.getPeriod().getDateTo());
        assertTrue(intern instanceof Intern);
        assertEquals("team_01", intern.getTeam().getTeamId());
        assertEquals("team_name_01", intern.getTeam().getName());
        assertEquals("teamLeader_01", intern.getTeam().getTeamLeader().getDomainId());
        assertEquals("XX项目负责人", intern.getTeam().getTeamLeader().getUserName());
        assertEquals("teamLeader_01@oocl.com", intern.getTeam().getTeamLeader().getEmail());

        UserPo hrPo = new UserPo();
        hrPo.setDomainId("hr");
        hrPo.setName("hr name");
        hrPo.setEmail("hr@oocl.com");
        hrPo.setUserType(UserType.HR);
        User hr = profileFactory.getUser(hrPo);
        assertEquals(hrPo.getEmail(), hr.getEmail());
        assertEquals(hrPo.getName(), hr.getUserName());
        assertEquals(hrPo.getDomainId(), hr.getDomainId());

        UserPo adminPo = new UserPo();
        adminPo.setDomainId("admin");
        adminPo.setName("admin name");
        adminPo.setEmail("admin@oocl.com");
        adminPo.setUserType(UserType.HR);
        User adminDo = profileFactory.getUser(adminPo);
        assertEquals(adminPo.getEmail(), adminDo.getEmail());
        assertEquals(adminPo.getName(), adminDo.getUserName());
        assertEquals(adminPo.getDomainId(), adminDo.getDomainId());
    }
}