package com.project.vestiart.controllers.authent;

import com.project.vestiart.config.JwtTokenProvider;
import com.project.vestiart.dto.AuthResponseDTO;
import com.project.vestiart.input.LoginDTO;
import com.project.vestiart.input.RegisterDTO;
import com.project.vestiart.models.Role;
import com.project.vestiart.models.User;
import com.project.vestiart.services.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthentController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserServiceImpl userService;

    public AuthentController(JwtTokenProvider jwtTokenProvider, UserServiceImpl userService, AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDto) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Gather the infos on user
            String userName = loginDto.getUsername();
            User user = userService.getUser(loginDto.getUsername());

            List<Role> roles = userService.getUserRoles(user);

            List<String> rolesAsString = roles.stream().map(Role::getName).toList();

            // Create security token for user
            String token = jwtTokenProvider.generateToken(userName, rolesAsString);

            return ResponseEntity.ok(new AuthResponseDTO(userName, token, roles.getFirst()));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO userInfos) {
        userService.registerNewUserAccount(userInfos);
        return ResponseEntity.ok("User registered successfully");
    }

}
