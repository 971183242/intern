package com.oocl.workshop.intern.interfaces.dto.profile;

import com.oocl.workshop.intern.domain.profile.entity.UserType;

public class EmployeeDTO extends UserDTO {

    public EmployeeDTO() {
        setUserType(UserType.EMPLOYEE);
    }
}
