package com.project.vestiart.dto;

import com.project.vestiart.enums.TypeEnum;
import lombok.*;

@Builder
public record IdeaDTO(String title, String description, String imageUrl, String tag1, String tag2, TypeEnum type) { }
