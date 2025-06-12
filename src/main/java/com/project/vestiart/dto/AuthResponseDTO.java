package com.project.vestiart.dto;

import com.project.vestiart.models.Role;
import lombok.Builder;

@Builder
public record AuthResponseDTO
        (
            String username,
            String tokenJwt,
            Role role
        ){ }