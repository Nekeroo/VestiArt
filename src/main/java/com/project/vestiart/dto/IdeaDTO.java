package com.project.vestiart.dto;

import com.project.vestiart.enums.TypeEnum;
import lombok.*;

import java.time.LocalDateTime;

@Builder
public record IdeaDTO(
        String title,
        String description,
        String imageUrl,
        String idExterne,
        String tag1,
        String tag2,
        LocalDateTime dateCreate,
        TypeEnum type) { }
