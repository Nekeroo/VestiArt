package com.project.vestiart.dto;

import com.project.vestiart.models.Role;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String username;
    private String token;
    private List<RoleDTO> roles;

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", tokenJwt='" + token + '\'' +
                ", roles=" + roles +
                '}';
    }
}
