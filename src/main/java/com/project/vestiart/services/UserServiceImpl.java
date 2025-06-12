package com.project.vestiart.services;

import com.project.vestiart.input.RegisterDTO;
import com.project.vestiart.models.Role;
import com.project.vestiart.models.User;
import com.project.vestiart.repositories.UserRepository;
import com.project.vestiart.services.interfaces.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User getUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public List<Role> getUserRoles(User user) {
        return List.of();
    }

    @Override
    public User registerNewUserAccount(RegisterDTO userInfos) {
        User user = User.builder()
                .username(userInfos.getUsername())
                .password(bCryptPasswordEncoder.encode(userInfos.getPassword()))
                .build();
        return userRepository.save(user);
    }
}
