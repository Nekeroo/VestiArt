package com.project.vestiart.models.dto;

import lombok.*;

@Builder
public record IdeaDTO(String title, String description, String imageUrl, String tag1, String tag2) { }
