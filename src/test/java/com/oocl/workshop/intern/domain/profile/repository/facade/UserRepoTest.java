package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import com.oocl.workshop.intern.domain.profile.repository.po.BaseUserPo;
import com.oocl.workshop.intern.domain.profile.repository.po.HRPo;
import com.oocl.workshop.intern.domain.profile.repository.po.InternPo;
import com.oocl.workshop.intern.domain.profile.repository.po.SuperAdminPo;
import com.oocl.workshop.intern.domain.profile.repository.po.TeamLeaderPo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class UserRepoTest {
    static Logger logger = LoggerFactory.getLogger(UserRepoTest.class);

    @Autowired
    UserRepo userRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void before() {
        SuperAdminPo superAdmin = new SuperAdminPo();
        superAdmin.setDomainId("superadmin");
        superAdmin.setEmail("superadmin@oocl.com");
        superAdmin.setName("超级管理员");
        this.entityManager.persist(superAdmin);

        HRPo hr = new HRPo();
        hr.setDomainId("hr");
        hr.setEmail("hr@oocl.com");
        hr.setName("人事经理");
        this.entityManager.persist(hr);

        TeamLeaderPo teamLeaderPo = new TeamLeaderPo();
        teamLeaderPo.setDomainId("teamLeader");
        teamLeaderPo.setEmail("teamLeader@oocl.com");
        teamLeaderPo.setName("XX项目负责人");
        this.entityManager.persist(teamLeaderPo);

        InternPo internPo0 = new InternPo();
        internPo0.setDomainId("intern_0");
        internPo0.setEmail("intern_0@oocl.com");
        internPo0.setName("实习生甲");
        InternPeriod period0 = new InternPeriod();
        period0.setDateFrom(new Date(120, 0, 1));
        period0.setDateTo(new Date(120, 5, 1));
        internPo0.setPeriod(period0);
        this.entityManager.persist(internPo0);

        InternPo internPo1 = new InternPo();
        internPo1.setDomainId("intern_1");
        internPo1.setEmail("intern_1@oocl.com");
        internPo1.setName("实习生乙");
        InternPeriod period1 = new InternPeriod();
        period1.setDateFrom(new Date(120, 3, 1));
        period1.setDateTo(new Date(120, 5, 1));
        internPo1.setPeriod(period1);
        this.entityManager.persist(internPo1);
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
    public void testFindByUserId() {
        BaseUserPo admin = userRepo.findByUserId(1L);
        logger.info("domainId:" + admin.getDomainId());
        logger.info("email:" + admin.getEmail());
        logger.info("name:" + admin.getName());
        assertNotNull(admin.getEmail());
        assertNotNull(admin.getName());
        assertNotNull(admin.getDomainId());
    }
}
