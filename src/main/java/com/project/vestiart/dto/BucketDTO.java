package com.project.vestiart.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BucketDTO(String idExterne, String tag1, String tag2, String tag3, String urlFile, LocalDateTime dateTime) { }
