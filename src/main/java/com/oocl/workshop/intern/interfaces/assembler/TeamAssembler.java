package com.oocl.workshop.intern.interfaces.assembler;

import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.interfaces.dto.profile.TeamDTO;

public class TeamAssembler {
    public static TeamDTO toDTO(Team team) {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamId(team.getTeamId());
        teamDTO.setName(team.getName());
        teamDTO.setTeamLeader(EmployeeAssembler.toDTO(team.getTeamLeader()));
        return teamDTO;
    }
}
