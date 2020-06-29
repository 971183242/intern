package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.entity.Role;
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
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
@Sql({"/sqls/insert-team.sql", "/sqls/insert-user.sql"})
public class UserRepoTest {
    @Autowired
    UserRepo userRepo;

    @Test
    public void testSuperAdmin() {
        UserPo admin = userRepo.findById("superadmin").get();
        assertEquals("superadmin@oocl.com", admin.getEmail());
        assertEquals("超级管理员", admin.getName());
        assertEquals("superadmin", admin.getDomainId());
        assertEquals(UserType.EMPLOYEE, admin.getUserType());
        assertEquals(Role.SUPER_ADMIN.getFullName(), admin.getRole());
    }

    @Test
    public void findByUserType() {
        List<UserPo> interns = userRepo.findByUserType(UserType.INTERN);
        assertEquals(2, interns.size());
    }


    @Test
    public void findByUserTypeAndRoleContains() {
        List<UserPo> leaders = userRepo.findByUserTypeAndRoleContains(UserType.EMPLOYEE, Role.TEAM_LEADER.getFullName());
        assertEquals(1, leaders.size());
        assertEquals("teamLeader", leaders.get(0).getDomainId());
    }

    @Test
    public void findByUserTypeAndTeamId() {
        List<UserPo> users = userRepo.findByUserTypeAndTeamId(UserType.INTERN, "TMS");
        assertEquals(1, users.size());
        assertEquals("intern_1", users.get(0).getDomainId());
    }

    @Test
    void findTeamActiveInterns() {
        List<UserPo> users = userRepo.findTeamActiveInterns("TMS", new Date(120,5,1), new Date(120,5,1));
        assertEquals(1, users.size());
        assertEquals("intern_1", users.get(0).getDomainId());
    }
}
