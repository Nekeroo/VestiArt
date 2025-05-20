package com.project.vestiart.models.dto;

import lombok.*;

@Builder
public record IdeaDTO(String title, String description, byte[] image) { }
