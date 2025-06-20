package com.project.vestiart.utils.mappers;

import com.project.vestiart.dto.RoleDTO;
import com.project.vestiart.models.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleMapper {

    public List<RoleDTO> mapRolesToRolesDTO(List<Role> roles) {

        return roles.stream().map((role -> RoleDTO.builder()
                .name(role.getName())
                .build())).toList();
    }

}
