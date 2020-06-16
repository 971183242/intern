package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import com.oocl.workshop.intern.domain.profile.repository.po.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepoTest {
    static Logger logger = LoggerFactory.getLogger(UserRepoTest.class);

    @Autowired
    UserRepo userRepo;

    @Autowired
    TeamLeaderRepo teamLeaderRepo;

    @BeforeAll
    public void before() {
        SuperAdminPo superAdmin = new SuperAdminPo();
        superAdmin.setDomainId("superadmin");
        superAdmin.setEmail("superadmin@oocl.com");
        superAdmin.setName("超级管理员");
        userRepo.save(superAdmin);

        HRPo hr = new HRPo();
        hr.setDomainId("hr");
        hr.setEmail("hr@oocl.com");
        hr.setName("人事经理");
        userRepo.save(hr);

        TeamLeaderPo teamLeaderPo = new TeamLeaderPo();
        teamLeaderPo.setDomainId("teamLeader");
        teamLeaderPo.setEmail("teamLeader@oocl.com");
        teamLeaderPo.setName("XX项目负责人");
        userRepo.save(teamLeaderPo);

        InternPo internPo0 = new InternPo();
        internPo0.setDomainId("intern_0");
        internPo0.setEmail("intern_0@oocl.com");
        internPo0.setName("实习生甲");
        InternPeriod period0 = new InternPeriod();
        period0.setDateFrom(new Date(120, 0, 1));
        period0.setDateTo(new Date(120, 5, 1));
        internPo0.setPeriod(period0);
        userRepo.save(internPo0);

        InternPo internPo1 = new InternPo();
        internPo1.setDomainId("intern_1");
        internPo1.setEmail("intern_1@oocl.com");
        internPo1.setName("实习生乙");
        InternPeriod period1 = new InternPeriod();
        period1.setDateFrom(new Date(120, 3, 1));
        period1.setDateTo(new Date(120, 5, 1));
        internPo1.setPeriod(period1);
        userRepo.save(internPo1);
    }

    @Test
    public void testFindAll() {
        List<BaseUserPo> users = userRepo.findAll();
        assertEquals(5, users.size());
    }

    @Test
    public void testSuperAdmin() {
        BaseUserPo admin = userRepo.findUserByDomainId("superadmin");
        assertEquals("superadmin@oocl.com", admin.getEmail());
        assertEquals("超级管理员", admin.getName());
        assertEquals("superadmin", admin.getDomainId());
        assertTrue(admin instanceof SuperAdminPo);
    }

    @Test
    public void testTeamLeader() {
        BaseUserPo admin = userRepo.findUserByDomainId("teamLeader");
        assertEquals("teamLeader@oocl.com", admin.getEmail());
        assertEquals("XX项目负责人", admin.getName());
        assertEquals("teamLeader", admin.getDomainId());
        assertTrue(admin instanceof TeamLeaderPo);
    }

    @Test
    public void testUpdateIntern() {
        InternPo intern0 = userRepo.findInternByDomainId("intern_0");
        intern0.getPeriod().setDateTo(new Date(120, 6, 1));
        intern0.setName("实习生甲+");

        userRepo.save(intern0);

        assertEquals(5, userRepo.count());
        BaseUserPo user = userRepo.findUserByDomainId("intern_0");
        logger.info("email:" + user.getEmail());
        logger.info("name:" + user.getName());
        logger.info("period from:" + ((InternPo)user).getPeriod().getDateFrom());
        logger.info("period to:" + ((InternPo)user).getPeriod().getDateTo());
        assertEquals("实习生甲+", user.getName());
        assertEquals(new Date(120, 6, 1), ((InternPo)user).getPeriod().getDateTo());
    }

    @Test
    public void testTeamLeaders() {
        List<TeamLeaderPo> leaderPos = teamLeaderRepo.findAll();
        assertEquals(1, leaderPos.size());
    }

    @Test
    public void testFindByUserId() {
        Optional<BaseUserPo> user = userRepo.findById(1L);
        logger.info("domainId:" + user.get().getDomainId());
        logger.info("email:" + user.get().getEmail());
        logger.info("name:" + user.get().getName());
        assertNotNull(user.get().getDomainId());
        assertNotNull(user.get().getEmail());
        assertNotNull(user.get().getName());
    }
}
