package com.project.vestiart.dto;

import com.project.vestiart.models.Role;
import lombok.Builder;

import java.util.List;

@Builder
public record AuthResponseDTO
        (
            String username,
            String tokenJwt,
            List<String> role
        ){ }