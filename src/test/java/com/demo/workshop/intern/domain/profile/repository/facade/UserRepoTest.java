package com.demo.workshop.intern.domain.profile.repository.facade;

import com.demo.workshop.intern.domain.profile.entity.Role;
import com.demo.workshop.intern.domain.profile.repository.po.UserPo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql({"classpath:test-sqls/insert-team.sql","classpath:test-sqls/insert-user.sql"})
public class UserRepoTest {
    @Autowired
    UserRepo userRepo;

    @Test
    public void testSuperAdmin() {
        UserPo admin = userRepo.findById("superadmin").get();
        assertEquals("superadmin@oocl.com", admin.getEmail());
        assertEquals("超级管理员", admin.getName());
        assertEquals("superadmin", admin.getDomainId());
        Assertions.assertEquals(Role.SUPER_ADMIN.getFullName(), admin.getRole());
    }


    @Test
    public void findByUserTypeAndRoleContains() {
        List<UserPo> leaders = userRepo.findByRoleContains(Role.TEAM_LEADER.getFullName());
        assertEquals(5, leaders.size());
    }


    @Test
    void findTeamActiveInterns() {
        List<UserPo> users = userRepo.findTeamActiveInterns("TMS-TEST", new Date(120,5,1), new Date(120,5,1));
        assertEquals(1, users.size());
        assertEquals("intern_1", users.get(0).getDomainId());
    }

    @Test
    void findActiveInterns() {
        List<UserPo> users = userRepo.findActiveInterns(new Date(120, 4, 21), new Date(120, 5, 20));
        assertEquals(3, users.size());
    }
}