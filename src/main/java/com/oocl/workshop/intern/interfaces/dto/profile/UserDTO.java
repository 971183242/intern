package com.oocl.workshop.intern.interfaces.dto.profile;

import com.oocl.workshop.intern.domain.profile.entity.UserType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class UserDTO {
    private UserType userType;
    private String domainId;
    private String email;
    private String name;

    private List<String> roles = new ArrayList<>();
}
