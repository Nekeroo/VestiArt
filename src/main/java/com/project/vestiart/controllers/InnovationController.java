package com.project.vestiart.controllers;

import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.input.RequestInputDTO;
import com.project.vestiart.models.CustomUserDetails;
import com.project.vestiart.models.Message;
import com.project.vestiart.models.User;
import com.project.vestiart.services.InnovationServiceImpl;
import com.project.vestiart.services.interfaces.JwtService;
import com.project.vestiart.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/innovation")
public class InnovationController {

    private final InnovationServiceImpl innovationService;
    private final JwtService jwtService;
    private final UserService userService;

    public InnovationController(InnovationServiceImpl innovationService, JwtService jwtService, UserService userService) {
        this.innovationService = innovationService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMultipleIdeasFromRequest(
            @RequestBody List<RequestInputDTO> inputs,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.builder().content("Unauthorized").build());
        }

        User user = userService.getUser(userDetails.getUsername());

        List<IdeaDTO> ideaDTOS = innovationService.createMultipleIdeas(inputs, user);

        return ResponseEntity.ok().body(ideaDTOS);
    }

}
