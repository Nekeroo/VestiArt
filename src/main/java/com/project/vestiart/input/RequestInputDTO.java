package com.project.vestiart.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for fashion innovation idea requests")
public class RequestInputDTO {

    @Schema(description = "Target demographic or person type", example = "Young urban professionals", required = true)
    private String person;

    @Schema(description = "Reference or inspiration for the innovation", example = "Sustainable materials and minimalist design")
    private String reference;

    @Schema(description = "Type of fashion item/collection", example = "Casual wear", required = true)
    private String type;

}
