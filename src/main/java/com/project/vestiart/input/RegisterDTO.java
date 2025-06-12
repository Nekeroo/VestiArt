package com.project.vestiart.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RegisterDTO {

    private String username;
    private String password;
}
