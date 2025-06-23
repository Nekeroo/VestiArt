package com.project.vestiart.dto;

import com.project.vestiart.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object for user information")
public class UserDTO {

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;
    
    @Schema(description = "JWT authentication token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "List of roles assigned to the user")
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
