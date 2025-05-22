package com.project.vestiart.models.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BucketDTO(String idExterne, String tag1, String tag2, String tag3, String urlFile) {

}
