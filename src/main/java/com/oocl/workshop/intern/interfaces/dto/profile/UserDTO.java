package com.oocl.workshop.intern.interfaces.dto.profile;

import com.oocl.workshop.intern.domain.profile.entity.UserType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {
    private UserType userType;
    private String domainId;
    private String email;
    private String name;
    private String internPeriodFromDate;
    private String internPeriodToDate;
    private TeamDTO team;
    private List<String> roles = new ArrayList<>();
}
