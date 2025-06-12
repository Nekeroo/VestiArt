package com.project.vestiart.enums;

import com.project.vestiart.models.Role;
import lombok.Getter;

@Getter
public enum RoleEnum {

    USER(new Role(1, "User")),
    ADMIN(new Role(2, "Admin")),;

    private Role role;

    RoleEnum(Role role) {
        this.role = role;
    }


}
