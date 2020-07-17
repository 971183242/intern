package com.oocl.workshop.intern.interfaces.assembler;

import com.google.common.collect.Lists;
import com.oocl.workshop.intern.domain.profile.entity.Employee;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.Role;
import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.domain.profile.entity.UserType;
import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import com.oocl.workshop.intern.interfaces.dto.profile.TeamDTO;
import com.oocl.workshop.intern.interfaces.dto.profile.UserDTO;
import com.oocl.workshop.intern.support.util.DateUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ProfileAssembler {

    public static UserDTO toDTO(User user) {
        if (user instanceof Intern) {
            return toInternDTO((Intern) user);
        } else if (user instanceof Employee) {
            return toEmployeeDTO((Employee) user);
        }
        return null;
    }


    private static UserDTO toInternDTO(Intern intern) {
        UserDTO dto = new UserDTO();
        setCommon(dto, intern);

        dto.setTeam(toTeamDTO(intern.getTeam()));

        if (intern.getPeriod() != null) {
            dto.setInternPeriodFromDate(DateUtil.formatDate(intern.getPeriod().getDateFrom()));
            dto.setInternPeriodToDate(DateUtil.formatDate(intern.getPeriod().getDateTo()));
        }
        return dto;
    }


    private static UserDTO toEmployeeDTO(Employee employee) {
        UserDTO dto = new UserDTO();
        setCommon(dto, employee);
        return dto;
    }


    private static void setCommon(UserDTO userDTO, User user) {
        userDTO.setDomainId(user.getDomainId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserType(UserType.EMPLOYEE);

        userDTO.getRoles().addAll(user.getRoles()
                .stream()
                .map(Role::getFullName)
                .collect(toList()));
    }


    public static User toDO(UserDTO dto) throws ParseException {
        if (UserType.INTERN.equals(dto.getUserType())) {
            return createIntern(dto);
        } else if (UserType.EMPLOYEE.equals(dto.getUserType())) {

        }
        return null;
    }


    private static Intern createIntern(UserDTO dto) throws ParseException {
        Intern user = new Intern();
        setCommon(user, dto);
        user.setRoles(Lists.newArrayList(Role.INTERN));

        InternPeriod internPeriod = new InternPeriod();
        if (!StringUtils.isEmpty(dto.getInternPeriodFromDate())) {
            internPeriod.setDateFrom(DateUtil.parseDate(dto.getInternPeriodFromDate()));
        }
        if (!StringUtils.isEmpty(dto.getInternPeriodFromDate())) {
            internPeriod.setDateTo(DateUtil.parseDate(dto.getInternPeriodToDate()));
        }
        user.setPeriod(internPeriod);
        return user;
    }


    private static Employee createEmployee(UserDTO dto) {
        Employee employee = new Employee();
        setCommon(employee, dto);
        return employee;
    }


    private static void setCommon(User user, UserDTO dto) {
        user.setName(dto.getName());
        user.setDomainId(dto.getDomainId());
        user.setEmail(dto.getEmail());
        user.setRoles(toRoleEntity(dto.getRoles()));
    }


    private static List<Role> toRoleEntity(List<String> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return new ArrayList<>(0);
        }
        return roles.stream()
                .map(Role::getRole)
                .filter(Objects::nonNull)
                .collect(toList());
    }


    public static TeamDTO toTeamDTO(Team team) {
        if (Objects.isNull(team)) {
            return null;
        }
        TeamDTO dto = new TeamDTO();
        dto.setTeamId(team.getTeamId());
        dto.setName(team.getName());
        dto.setTeamLeader(toDTO(team.getTeamLeader()));
        return dto;
    }
}
