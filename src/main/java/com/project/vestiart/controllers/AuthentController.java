package com.project.vestiart.controllers;

import com.project.vestiart.config.jwt.JwtTokenProvider;
import com.project.vestiart.dto.UserDTO;
import com.project.vestiart.input.LoginDTO;
import com.project.vestiart.input.RegisterDTO;
import com.project.vestiart.models.Message;
import com.project.vestiart.models.Role;
import com.project.vestiart.models.User;
import com.project.vestiart.services.JwtServiceImpl;
import com.project.vestiart.services.database.UserServiceImpl;
import com.project.vestiart.services.interfaces.JwtService;
import com.project.vestiart.utils.mappers.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthentController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    private final JwtService jwtService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public AuthentController(JwtTokenProvider jwtTokenProvider, UserServiceImpl userService, AuthenticationManager authenticationManager, UserMapper userMapper, JwtServiceImpl jwtService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
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

            List<String> roles = userService.getUserRoles(user);

            String token = jwtTokenProvider.generateToken(userName, roles);

            UserDTO userDTO = userMapper.mapUserToUserDTO(user, token);
            return ResponseEntity.ok().body(userDTO);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(Message.builder().content("Invalid username or password").build());
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO userInfos) {

        if (this.userService.isAnyUserWithThisUsername(userInfos.getUsername())) {
            return ResponseEntity.badRequest().body(Message.builder().content("Username is already taken!").build());
        }

        User user = userService.registerNewUserAccount(userInfos);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userInfos.getUsername(), userInfos.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        UserDTO userDTO = userMapper.mapUserToUserDTO(user, token);
        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(value = "Authorization",required = false) String authorizationHeader) {

        if (authorizationHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.builder().content("Unauthorized").build());
        }

        try {
            String token = authorizationHeader.replace("Bearer ", "");

            User user = jwtService.retrieveUserByToken(token);

            UserDTO userDTOREsult = userMapper.mapUserToUserDTO(user, token);

            return ResponseEntity.ok().body(userDTOREsult);
        }

        catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.builder().content("Unauthorized").build());
        }
    }

}
