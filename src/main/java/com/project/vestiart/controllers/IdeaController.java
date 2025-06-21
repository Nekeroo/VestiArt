package com.project.vestiart.controllers;

import com.project.vestiart.dto.RetrieveIdeaDTO;
import com.project.vestiart.models.Idea;
import com.project.vestiart.models.Message;
import com.project.vestiart.models.User;
import com.project.vestiart.services.JwtServiceImpl;
import com.project.vestiart.services.database.BucketService;
import com.project.vestiart.services.interfaces.IdeaService;
import com.project.vestiart.services.interfaces.JwtService;
import com.project.vestiart.services.interfaces.UserService;
import com.project.vestiart.utils.mappers.IdeaMapper;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/idea")
@Tag(name = "Idea", description = "Idea management APIs")
public class IdeaController {

    private final IdeaMapper ideaMapper;
    private final BucketService bucketService;
    private final IdeaService ideaService;

    private final JwtService jwtService;

    private final UserService userService;

    public IdeaController(IdeaMapper ideaMapper, BucketService bucketService, IdeaService ideaService, JwtService jwtService, UserService userService) {
        this.ideaService = ideaService;
        this.ideaMapper = ideaMapper;
        this.bucketService = bucketService;
        this.jwtService = jwtService;
        this.userService = userService;
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
        Optional<Idea> idea = ideaService.getIdeaByIdExterne(uid);

        if (idea.isEmpty()) {
            return ResponseEntity.badRequest().body("Idea not found");
        }

        return ResponseEntity.ok(idea.get());
    }

    @Operation(summary = "Delete an idea", description = "Remove an idea from the system by its UID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Idea successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Idea not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/delete/{uid}")
    public ResponseEntity<?> remove(
            @Parameter(description = "Unique identifier of the idea to delete") @PathVariable String uid) {
        Optional<Idea> idea = ideaService.getIdeaByIdExterne(uid);
        if (idea.isPresent()) {
            ideaService.removeIdea(idea.get());
            bucketService.deleteDocumentFromTheBucket(uid);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().body("Idea not found");
        }
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

        RetrieveIdeaDTO ideas = ideaService.getIdeasAfterDynamic(start, nbElement, sortKey);

        if (ideas.getIdeas().isEmpty()) {
            return ResponseEntity.ok().body(RetrieveIdeaDTO.builder()
                    .ideas(new ArrayList<>())
                    .nextKey(null)
                    .build());
        }

        return ResponseEntity.ok(ideas);
    }

    @GetMapping("/retrieve/mine")
    public ResponseEntity<?> retrieveMyIdeas(
         @Parameter(description = "Starting index for pagination") @RequestParam int start,
         @Parameter(description = "Number of elements to retrieve") @RequestParam int nbElement,
         @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.builder().content("Unauthorized").build());
        }

        String token = authorizationHeader.replace("Bearer ", "");

        User user = jwtService.retrieveUserByToken(token);
        // TODO : A finir

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.builder().content("Unauthorized").build());
        }

        RetrieveIdeaDTO ideas = ideaService.getIdeasFromIdUser(user.getId(), start, nbElement);
        return ResponseEntity.ok("This feature is not yet implemented.");
    }


}
