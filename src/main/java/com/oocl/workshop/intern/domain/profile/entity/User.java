package com.oocl.workshop.intern.domain.profile.entity;

import lombok.Data;

@Data
public abstract class User {
    private Long userId;
    private String domainId;
    private String email;
    private String userName;

    private Team team;
}
