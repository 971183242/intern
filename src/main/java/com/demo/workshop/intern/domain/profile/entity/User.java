package com.demo.workshop.intern.domain.profile.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class User {
    private String domainId;
    private String email;
    private String name;
    private List<Role> roles = new ArrayList<>();
}
