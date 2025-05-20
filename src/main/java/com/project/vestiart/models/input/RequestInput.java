package com.project.vestiart.models.input;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestInput {

    private String person;
    private String reference;
    private int type;


}
