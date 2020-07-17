package com.oocl.workshop.intern.interfaces.assembler;

import com.oocl.workshop.intern.domain.profile.entity.Employee;
import com.oocl.workshop.intern.domain.profile.entity.Role;
import com.oocl.workshop.intern.interfaces.dto.profile.UserDTO;

import static com.oocl.workshop.intern.domain.profile.entity.UserType.EMPLOYEE;
import static java.util.stream.Collectors.toList;

public class EmployeeAssembler {
    public static UserDTO toDTO(Employee employee) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserType(EMPLOYEE);
        userDTO.setDomainId(employee.getDomainId());
        userDTO.setEmail(employee.getEmail());
        userDTO.setName(employee.getName());
        userDTO.setRoles(employee.getRoles().stream().map(Role::getFullName).collect(toList()));
        return userDTO;
    }
}
