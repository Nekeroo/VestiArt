package com.project.vestiart.services.database;

import com.project.vestiart.input.RegisterDTO;
import com.project.vestiart.models.Role;
import com.project.vestiart.models.User;
import com.project.vestiart.repositories.UserRepository;
import com.project.vestiart.services.interfaces.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleServiceImpl roleServiceImpl;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleServiceImpl roleServiceImpl) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleServiceImpl = roleServiceImpl;
    }

    @Override
    public User getUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    public boolean isAnyUserWithThisUsername(String username) {
        return this.userRepository.existsUserByUsername(username);
    }

    public List<String> getUserRoles(User user) {
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }

    @Override
    public User registerNewUserAccount(RegisterDTO userInfos) {
        Role role = roleServiceImpl.getRoleById(1);

        User user = User.builder()
                .username(userInfos.getUsername())
                .password(bCryptPasswordEncoder.encode(userInfos.getPassword()))
                .roles(List.of(role))
                .build();

        return userRepository.save(user);
    }
}
