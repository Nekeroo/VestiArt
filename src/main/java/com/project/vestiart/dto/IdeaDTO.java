package com.project.vestiart.dto;

import com.project.vestiart.enums.TypeEnum;
import com.project.vestiart.models.File;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Data Transfer Object for a fashion idea")
public record IdeaDTO(
        @Schema(description = "Title of the idea", example = "Eco-friendly Denim Collection")
        String title,

        @Schema(description = "Detailed description of the idea", example = "A collection of sustainable denim using recycled materials and eco-friendly dyes")
        String description,

        @Schema(description = "image of the idea")
        File image,

        @Schema(description = "Primary tag/category", example = "Sustainable")
        String tag1,

        @Schema(description = "Secondary tag/category", example = "Denim")
        String tag2,

        @Schema(description = "Date and time when the idea was created")
        LocalDateTime dateCreate,

        @Schema(description = "URL to the idea's detailed PDF document")
        File pdf,

        @Schema(description = "Type classification of the idea")
        TypeEnum type) { }
