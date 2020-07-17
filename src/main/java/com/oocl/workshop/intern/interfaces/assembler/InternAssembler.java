package com.oocl.workshop.intern.interfaces.assembler;

import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.Role;
import com.oocl.workshop.intern.interfaces.dto.profile.UserDTO;
import com.oocl.workshop.intern.support.util.DateUtil;

import static com.oocl.workshop.intern.domain.profile.entity.UserType.INTERN;
import static java.util.stream.Collectors.toList;

public class InternAssembler {
    public static UserDTO toDTO(Intern intern) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserType(INTERN);
        userDTO.setDomainId(intern.getDomainId());
        userDTO.setEmail(intern.getEmail());
        userDTO.setName(intern.getName());
        userDTO.setInternPeriodFromDate(DateUtil.formatDate(intern.getPeriod().getDateFrom()));
        userDTO.setInternPeriodToDate(DateUtil.formatDate(intern.getPeriod().getDateTo()));
        userDTO.setTeam(TeamAssembler.toDTO(intern.getTeam()));
        userDTO.setRoles(intern.getRoles().stream().map(Role::getFullName).collect(toList()));
        return userDTO;
    }
}
