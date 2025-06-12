package com.project.vestiart.controllers;

import com.project.vestiart.dto.RetrieveIdeaDTO;
import com.project.vestiart.models.Idea;
import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.services.BucketService;
import com.project.vestiart.services.interfaces.IdeaService;
import com.project.vestiart.utils.mappers.IdeaMapper;
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

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/idea")
@Tag(name = "Idea", description = "Idea management APIs")
public class IdeaController {

    private final IdeaMapper ideaMapper;
    private final BucketService bucketService;
    private final IdeaService ideaService;

    public IdeaController(IdeaMapper ideaMapper, BucketService bucketService, IdeaService ideaService) {
        this.ideaService = ideaService;
        this.ideaMapper = ideaMapper;
        this.bucketService = bucketService;
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

    @Operation(summary = "Add a new idea", description = "Create a new idea in the system")
    @ApiResponse(responseCode = "200", description = "Idea successfully created",
            content = @Content(schema = @Schema(implementation = IdeaDTO.class)))
    @PostMapping("/add")
    public IdeaDTO addIdea(@Parameter(description = "Idea details") @RequestBody IdeaDTO IdeaDTO) {
        Idea idea = ideaMapper.mapIdeaDTOToIdea(IdeaDTO);
        ideaService.saveIdea(idea);
        return IdeaDTO;
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

    @GetMapping("/retrieve")
    public ResponseEntity<?> retrieveIdeaWithParameters(@RequestParam int start, @RequestParam int nbElement, @RequestParam String sortKey) {

        RetrieveIdeaDTO ideas = ideaService.getIdeasAfterDynamic(start, nbElement, sortKey);

        if (ideas.getIdeas().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ideas);
    }


}
