package com.project.vestiart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for paginated ideas retrieval")
public class RetrieveIdeaDTO {

    @Schema(description = "List of fashion ideas")
    private List<IdeaDTO> ideas;
    
    @Schema(description = "Token or identifier for retrieving the next page of results, null if last page")
    private Object nextKey;

}
