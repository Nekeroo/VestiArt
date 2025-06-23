package com.project.vestiart.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login credentials data transfer object")
public class LoginDTO {

    @Schema(description = "Username for authentication", example = "john_doe", required = true)
    private String username;
    
    @Schema(description = "User password", example = "password123", required = true)
    private String password;

}
