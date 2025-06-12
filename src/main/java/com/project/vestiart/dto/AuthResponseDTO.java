package com.project.vestiart.dto;

import lombok.Builder;

@Builder
public record AuthResponseDTO
        (
            String username,
            String tokenJwt
        ){ }