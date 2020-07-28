package com.demo.workshop.intern.domain.profile.entity;

public enum Role {
    INTERN, TEAM_LEADER, HR, SUPER_ADMIN;

    public String getFullName() {
        return "ROLE_" + name();
    }

    public static Role getRole(String roleName) {
        if (INTERN.getFullName().equals(roleName)) {
            return INTERN;
        } else if (TEAM_LEADER.getFullName().equals(roleName)) {
            return TEAM_LEADER;
        } else if (HR.getFullName().equals(roleName)) {
            return HR;
        } else if (SUPER_ADMIN.getFullName().equals(roleName)) {
            return SUPER_ADMIN;
        }
        return null;
    }

    public static boolean isIntern(String fullRoleName) {
        return INTERN.getFullName().equals(fullRoleName);
    }
}
