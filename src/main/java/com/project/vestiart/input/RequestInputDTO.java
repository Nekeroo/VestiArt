package com.project.vestiart.input;


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
