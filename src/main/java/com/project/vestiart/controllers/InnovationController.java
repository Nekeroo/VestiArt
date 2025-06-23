package com.project.vestiart.controllers;

import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.input.RequestInputDTO;
import com.project.vestiart.models.Message;
import com.project.vestiart.models.User;
import com.project.vestiart.services.InnovationServiceImpl;
import com.project.vestiart.services.interfaces.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/innovation")
public class InnovationController {

    private final InnovationServiceImpl innovationService;
    private final JwtService jwtService;

    public InnovationController(InnovationServiceImpl innovationService, JwtService jwtService) {
        this.innovationService = innovationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMultipleIdeasFromRequest(
            @RequestBody List<RequestInputDTO> inputs,
            @RequestHeader(name = "Authorization", required = false) String token) {

        if (token == null || token.isEmpty() || !jwtService.isUserValid(token)) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.builder().content("Unauthorized").build());
        }

        User user = jwtService.retrieveUserByToken(token);

        List<IdeaDTO> ideaDTOS = innovationService.createMultipleIdeas(inputs, user);

        return ResponseEntity.ok().body(ideaDTOS);
    }
}
