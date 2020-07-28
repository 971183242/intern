package com.demo.workshop.intern.interfaces.dto.profile;

import lombok.Data;

@Data
public class InternDTO extends UserDTO {
    private String internPeriodFromDate;
    private String internPeriodToDate;
    private TeamDTO team;

    public InternDTO() {
    }
}
