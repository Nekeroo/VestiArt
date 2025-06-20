package com.project.vestiart.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestInputDTO {

    private String person;

    private String reference;

    private String type;

}
