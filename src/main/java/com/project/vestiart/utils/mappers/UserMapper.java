package com.project.vestiart.utils.mappers;

import com.project.vestiart.dto.UserDTO;
import com.project.vestiart.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserDTO mapUserToUserDTO(User user, String token) {
        return UserDTO.builder()
                .username(user.getUsername())
                .token(token)
                .roles(user.getRoles())
                .build();
    }

}
