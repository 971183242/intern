package com.oocl.workshop.intern.domain.profile.service;

import com.oocl.workshop.intern.domain.profile.entity.HR;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.TeamLeader;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.domain.profile.entity.UserType;
import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import com.oocl.workshop.intern.domain.profile.repository.facade.UserRepo;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional(propagation = Propagation.NESTED)
@TestPropertySource("classpath:application-test.properties")
class ProfileFactoryTest {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProfileFactory profileFactory;


    @BeforeEach
    void setUp() {
        UserPo teamLeaderPo = new UserPo();
        teamLeaderPo.setDomainId("teamLeader");
        teamLeaderPo.setEmail("teamLeader@oocl.com");
        teamLeaderPo.setName("XX项目负责人");
        teamLeaderPo.setUserType(UserType.TeamLeader);
        userRepo.save(teamLeaderPo);

        UserPo internPo0 = new UserPo();
        internPo0.setDomainId("intern_0");
        internPo0.setEmail("intern_0@oocl.com");
        internPo0.setName("实习生甲");
        internPo0.setUserType(UserType.Intern);
        InternPeriod period0 = new InternPeriod();
        period0.setDateFrom(new Date(120, 0, 1));
        period0.setDateTo(new Date(120, 5, 1));
        internPo0.setInternPeriod(period0);
        userRepo.save(internPo0);
    }

    @Test
    void createPo() {
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

        HR hr = new HR();
        hr.setDomainId("hr");
        hr.setEmail("hr@oocl.com");
        hr.setUserName("人事经理");
        UserPo hrPo = profileFactory.createPo(hr);
        assertEquals(hr.getUserName(), hrPo.getName());
        assertEquals(hr.getDomainId(), hrPo.getDomainId());
        assertEquals(hr.getEmail(), hrPo.getEmail());
        assertEquals(UserType.HR, hrPo.getUserType());

    }

    @Test
    void getUser() {
        UserPo teamLeader = userRepo.findUserByDomainId("teamLeader");
        User user = profileFactory.getUser(teamLeader);
        assertEquals("XX项目负责人", user.getUserName());
        assertTrue(user instanceof TeamLeader);

        UserPo intern0 = userRepo.findUserByDomainId("intern_0");
        User intern = profileFactory.getUser(intern0);
        assertEquals("实习生甲", intern.getUserName());
        assertEquals(new Date(120, 5, 1), ((Intern) intern).getPeriod().getDateTo());
        assertTrue(intern instanceof Intern);
    }
}