package com.project.vestiart.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User registration data transfer object")
public class RegisterDTO {

    @Schema(description = "Username for the new account", example = "john_doe", required = true)
    private String username;
    
    @Schema(description = "Password for the new account", example = "securePassword123", required = true)
    private String password;
}
