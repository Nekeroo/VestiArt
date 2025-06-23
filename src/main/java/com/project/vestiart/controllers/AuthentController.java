package com.project.vestiart.controllers;

import com.project.vestiart.config.jwt.JwtTokenProvider;
import com.project.vestiart.dto.UserDTO;
import com.project.vestiart.input.LoginDTO;
import com.project.vestiart.input.RegisterDTO;
import com.project.vestiart.models.CustomUserDetails;
import com.project.vestiart.models.Message;
import com.project.vestiart.models.Role;
import com.project.vestiart.models.User;
import com.project.vestiart.services.database.UserServiceImpl;
import com.project.vestiart.utils.mappers.UserMapper;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "APIs for user authentication, registration and user information retrieval")
public class AuthentController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public AuthentController(JwtTokenProvider jwtTokenProvider, UserServiceImpl userService, AuthenticationManager authenticationManager, UserMapper userMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    @Operation(summary = "User login", description = "Authenticate a user with username and password to obtain a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = Message.class)))
    })
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

    @Operation(summary = "User registration", description = "Register a new user account and authenticate the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Username already exists",
                    content = @Content(schema = @Schema(implementation = Message.class)))
    })
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

    @Operation(summary = "Get current user information", description = "Retrieves the authenticated user's profile information using the JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing token",
                    content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @GetMapping("/me")
    public ResponseEntity<?> me(
            @Parameter(description = "Authenticated user details") @AuthenticationPrincipal CustomUserDetails userDetails, 
            @Parameter(description = "HTTP request containing the authorization header") HttpServletRequest request) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.builder().content("Unauthorized").build());
        }

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Message.builder().content("Unauthorized").build());
            }
            String token = authHeader.substring(7);

            User user = userService.getUser(userDetails.getUsername());

            UserDTO userDTOResult = userMapper.mapUserToUserDTO(user, token);

            return ResponseEntity.ok().body(userDTOResult);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Message.builder().content("Unauthorized").build());
        }
    }

}
