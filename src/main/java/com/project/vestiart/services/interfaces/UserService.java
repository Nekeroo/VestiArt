package com.project.vestiart.services.interfaces;

import com.project.vestiart.input.RegisterDTO;
import com.project.vestiart.models.Role;
import com.project.vestiart.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User getUser(String username);

    List<String> getUserRoles(User user);

    User registerNewUserAccount(RegisterDTO user);


}
