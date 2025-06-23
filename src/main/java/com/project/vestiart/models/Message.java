package com.project.vestiart.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Generic message response object used for API responses")
public class Message {

    @Schema(description = "Message content", example = "Operation completed successfully")
    private String content;

}
