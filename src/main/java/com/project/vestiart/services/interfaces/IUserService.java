package com.project.vestiart.services.interfaces;

import com.project.vestiart.input.RegisterDTO;
import com.project.vestiart.models.User;

import java.util.List;

public interface IUserService {

    User getUser(String username);

    List<String> getUserRoles(User user);

    User registerNewUserAccount(RegisterDTO user);

    long countUsers();
}
