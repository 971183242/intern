package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.entity.UserType;
import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableJpaAuditing
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepoTest {
    static Logger logger = LoggerFactory.getLogger(UserRepoTest.class);

    @Autowired
    UserRepo userRepo;

    @BeforeAll
    public void before() {
        UserPo superAdmin = new UserPo();
        superAdmin.setDomainId("superadmin");
        superAdmin.setEmail("superadmin@oocl.com");
        superAdmin.setName("超级管理员");
        superAdmin.setUserType(UserType.SuperAdmin);
        userRepo.save(superAdmin);

        UserPo hr = new UserPo();
        hr.setDomainId("hr");
        hr.setEmail("hr@oocl.com");
        hr.setName("人事经理");
        hr.setUserType(UserType.HR);
        userRepo.save(hr);

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

        UserPo internPo1 = new UserPo();
        internPo1.setDomainId("intern_1");
        internPo1.setEmail("intern_1@oocl.com");
        internPo1.setName("实习生乙");
        internPo1.setUserType(UserType.Intern);
        InternPeriod period1 = new InternPeriod();
        period1.setDateFrom(new Date(120, 3, 1));
        period1.setDateTo(new Date(120, 5, 1));
        internPo1.setInternPeriod(period1);
        userRepo.save(internPo1);
    }

    @Test
    public void testFindAll() {
        List<UserPo> users = userRepo.findAll();
        assertEquals(5, users.size());
    }

    @Test
    public void testSuperAdmin() {
        UserPo admin = userRepo.findById("superadmin").get();
        assertEquals("superadmin@oocl.com", admin.getEmail());
        assertEquals("超级管理员", admin.getName());
        assertEquals("superadmin", admin.getDomainId());
        assertEquals(UserType.SuperAdmin, admin.getUserType());
    }

    @Test
    public void testTeamLeader() {
        UserPo admin = userRepo.findById("teamLeader").get();
        assertEquals("teamLeader@oocl.com", admin.getEmail());
        assertEquals("XX项目负责人", admin.getName());
        assertEquals("teamLeader", admin.getDomainId());
        assertEquals(UserType.TeamLeader, admin.getUserType());
    }

    @Test
    public void testUpdateIntern() {
        UserPo intern0 = userRepo.findById("intern_0").get();
        intern0.getInternPeriod().setDateTo(new Date(120, 6, 1));
        intern0.setName("实习生甲+");

        userRepo.save(intern0);

        assertEquals(5, userRepo.count());
        UserPo user = userRepo.findById("intern_0").get();
        logger.info("email:" + user.getEmail());
        logger.info("name:" + user.getName());
        logger.info("period from:" + user.getInternPeriod().getDateFrom());
        logger.info("period to:" + user.getInternPeriod().getDateTo());
        assertEquals("实习生甲+", user.getName());
        assertEquals(new Date(120, 6, 1), user.getInternPeriod().getDateTo());
    }

    @Test
    public void testTeamLeaders() {
        List<UserPo> leaders = userRepo.findByUserType(UserType.TeamLeader);
        assertEquals(1, leaders.size());
    }

}
