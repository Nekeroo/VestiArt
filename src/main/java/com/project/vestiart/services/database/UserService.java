package com.project.vestiart.services.database;

import com.project.vestiart.input.RegisterDTO;
import com.project.vestiart.models.Role;
import com.project.vestiart.models.User;
import com.project.vestiart.repositories.IUserRepository;
import com.project.vestiart.services.interfaces.IUserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final IUserRepository IUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;

    public UserService(IUserRepository IUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService) {
        this.IUserRepository = IUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User getUser(String username) {
        return this.IUserRepository.findByUsername(username);
    }

    public boolean isAnyUserWithThisUsername(String username) {
        return this.IUserRepository.existsUserByUsername(username);
    }

    public List<String> getUserRoles(User user) {
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }

    @Override
    public User registerNewUserAccount(RegisterDTO userInfos) {
        Role role = roleService.getRoleById(1);

        User user = User.builder()
                .username(userInfos.getUsername())
                .password(bCryptPasswordEncoder.encode(userInfos.getPassword()))
                .roles(List.of(role))
                .build();

        return IUserRepository.save(user);
    }

    public long countUsers() {
        return IUserRepository.count();
    }

}
