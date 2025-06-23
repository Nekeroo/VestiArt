package com.project.vestiart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for user role information")
public class RoleDTO {

    @Schema(description = "Name of the role", example = "Admin")
    private String name;

}
