package com.project.vestiart.controllers;

import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.input.RequestInputDTO;
import com.project.vestiart.models.CustomUserDetails;
import com.project.vestiart.models.Message;
import com.project.vestiart.models.User;
import com.project.vestiart.services.InnovationService;
import com.project.vestiart.services.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/innovation")
@Tag(name = "Innovation", description = "APIs for creating and managing fashion innovations")
public class InnovationController {

    private final InnovationService innovationService;
    private final IUserService IUserService;

    public InnovationController(InnovationService innovationService, IUserService IUserService) {
        this.innovationService = innovationService;
        this.IUserService = IUserService;
    }

    @Operation(summary = "Create multiple ideas", description = "Generate multiple fashion innovation ideas based on the provided inputs",
              security = { @SecurityRequirement(name = "bearer-jwt") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ideas created successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = IdeaDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required",
                    content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<?> createMultipleIdeasFromRequest(
            @Parameter(description = "List of input specifications for idea generation", required = true)
            @RequestBody List<RequestInputDTO> inputs,
            @Parameter(description = "Authenticated user details")
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.builder().content("Unauthorized").build());
        }

        User user = IUserService.getUser(userDetails.getUsername());

        List<IdeaDTO> ideaDTOS = innovationService.createMultipleIdeas(inputs, user);

        return ResponseEntity.ok().body(ideaDTOS);
    }

}
