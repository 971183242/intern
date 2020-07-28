package com.demo.workshop.intern.interfaces.dto.profile;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class UserDTO {
    private String domainId;
    private String email;
    private String name;

    private List<String> roles = new ArrayList<>();
}
