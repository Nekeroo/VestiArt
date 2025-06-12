package com.project.vestiart.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveIdeaDTO {

    private List<IdeaDTO> ideas;
    private Object nextKey;

}
