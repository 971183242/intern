package com.oocl.workshop.intern.domain.profile.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public abstract class User {
    private String domainId;
    private String email;
    private String name;
    private List<Role> roles = new ArrayList<>();
}
