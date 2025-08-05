package com.project.vestiart.controllers;

import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.dto.RetrieveIdeaDTO;
import com.project.vestiart.models.CustomUserDetails;
import com.project.vestiart.models.Idea;
import com.project.vestiart.models.Message;
import com.project.vestiart.models.User;
import com.project.vestiart.services.database.RequestInputService;
import com.project.vestiart.services.interfaces.IIdeaService;
import com.project.vestiart.services.interfaces.IUserService;
import com.project.vestiart.utils.mappers.IdeaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/idea")
@Tag(name = "Idea", description = "Idea management APIs")
public class IdeaController {

    private final IdeaMapper ideaMapper;
    private final IIdeaService IIdeaService;

    private final IUserService IUserService;

    private final RequestInputService requestInputService;


    public IdeaController(IdeaMapper ideaMapper,  IIdeaService IIdeaService, IUserService IUserService, RequestInputService requestInputService) {
        this.IIdeaService = IIdeaService;
        this.ideaMapper = ideaMapper;
        this.IUserService = IUserService;
        this.requestInputService = requestInputService;
    }

    @Operation(summary = "Retrieve idea by UID", description = "Get a specific idea using its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Idea found",
                    content = @Content(schema = @Schema(implementation = Idea.class))),
            @ApiResponse(responseCode = "400", description = "Idea not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/retrieve/{uid}")
    public ResponseEntity<?> retrieIdeaByUid(
            @Parameter(description = "Unique identifier of the idea") @PathVariable String uid) {
        Optional<Idea> idea = IIdeaService.getIdeaFromId(uid);

        if (idea.isEmpty()) {
            return ResponseEntity.badRequest().body("Idea not found");
        }

        IdeaDTO ideaDTO = ideaMapper.mapIdeaToIdeaDTO(idea.get());

        return ResponseEntity.ok(ideaDTO);
    }

    @Operation(summary = "Delete an idea", description = "Remove an idea from the system by its UID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Idea successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Idea not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/delete/{uid}")
    public ResponseEntity<?> remove(
            @Parameter(description = "Unique identifier of the idea to delete") @PathVariable String uid,
            @AuthenticationPrincipal CustomUserDetails user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Message.builder().content("Unauthorized").build());
        }

        Optional<Idea> idea = IIdeaService.getIdeaFromId(uid);


        System.out.println(idea);
        if (idea.isEmpty()) {
            return ResponseEntity.badRequest().body("Idea not found");
        }

        Idea ideaEntity = idea.get();

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("Admin"));

        if (!isAdmin) {
            String currentUsername = user.getUsername();
            User userFound = IUserService.getUser(currentUsername);
            System.out.println(user);
            if (ideaEntity.getUser().getId() != userFound.getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Message.builder().content("Access denied: not the idea owner").build());
            }
        }

        requestInputService.deleteRequestInput(ideaEntity);
        IIdeaService.removeIdea(ideaEntity);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Retrieve ideas with pagination and sorting",
            description = "Get a paginated list of ideas with dynamic sorting capability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ideas found and returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RetrieveIdeaDTO.class))),
            @ApiResponse(responseCode = "204", description = "No ideas found for the given parameters")
    })
    @GetMapping("/retrieve")
    public ResponseEntity<?> retrieveIdeaWithParameters(
            @Parameter(description = "Starting index for pagination") @RequestParam int start,
            @Parameter(description = "Number of elements to retrieve") @RequestParam int nbElement,
            @Parameter(description = "Key to sort the ideas by") @RequestParam String sortKey) {

        RetrieveIdeaDTO ideas = IIdeaService.getIdeasAfterDynamic(start, nbElement, sortKey);

        if (ideas.getIdeas().isEmpty()) {
            return ResponseEntity.ok().body(RetrieveIdeaDTO.builder()
                    .ideas(new ArrayList<>())
                    .nextKey(null)
                    .build());
        }

        return ResponseEntity.ok(ideas);
    }

        @Operation(summary = "Retrieve ideas by type", description = "Get a paginated list of ideas filtered by the specified type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ideas successfully retrieved",
                    content = @Content(schema = @Schema(implementation = RetrieveIdeaDTO.class)))
    })
    @GetMapping("/retrieve/type")
    public ResponseEntity<?> retrieveIdeaFromType(
            @Parameter(description = "Starting index for pagination") @RequestParam int start,
            @Parameter(description = "Number of elements to retrieve") @RequestParam int nbElement,
            @Parameter(description = "Type of ideas to filter by") @RequestParam String type) {

        RetrieveIdeaDTO ideas = IIdeaService.getIdeaFromType(start, nbElement, type);

        if (ideas == null || ideas.getIdeas().isEmpty()) {
            return ResponseEntity.ok().body(RetrieveIdeaDTO.builder()
                    .ideas(new ArrayList<>())
                    .nextKey(null)
                    .build());
        }

        return ResponseEntity.ok(ideas);
    }


    @Operation(summary = "Retrieve user's ideas", description = "Get a paginated list of ideas created by the authenticated user",
              security = { @SecurityRequirement(name = "bearer-jwt") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's ideas successfully retrieved",
                    content = @Content(schema = @Schema(implementation = RetrieveIdeaDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required",
                    content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @GetMapping("/retrieve/mine")
    public ResponseEntity<?> retrieveMyIdeas(
            @Parameter(description = "Starting index for pagination") @RequestParam int start,
            @Parameter(description = "Number of elements to retrieve") @RequestParam int nbElement,
            @Parameter(description = "Authenticated user details") @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.builder().content("Unauthorized").build());
        }

        User user = IUserService.getUser(userDetails.getUsername());

        RetrieveIdeaDTO retrieveIdeaDTO = IIdeaService.getIdeasFromIdUser(user.getId(), start, nbElement);

        if (retrieveIdeaDTO.getIdeas().isEmpty()) {
            return ResponseEntity.ok().body(RetrieveIdeaDTO.builder()
                    .ideas(new ArrayList<>())
                    .nextKey(null)
                    .build());
        }

        return ResponseEntity.ok(retrieveIdeaDTO);
    }


}
