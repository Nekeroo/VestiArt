package com.project.vestiart.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PdfGenerationResponse {

    private List<String> pdfs;

}
