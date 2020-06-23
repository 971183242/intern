package com.oocl.workshop.intern.domain.profile.entity;

public enum Role {
    INTERN, TEAM_LEADER, HR, SUPER_ADMIN;

    public String getFullName() {
        return "ROLE_" + name();
    }
}
