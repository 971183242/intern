package com.oocl.workshop.intern.interfaces.dto.profile;

import lombok.Data;

@Data
public class TeamDTO {
    private String teamId;
    private String name;
    private UserDTO teamLeader;
}
