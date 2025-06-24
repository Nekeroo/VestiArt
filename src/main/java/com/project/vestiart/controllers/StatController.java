package com.project.vestiart.controllers;

import com.project.vestiart.dto.StatDTO;
import com.project.vestiart.services.interfaces.IIdeaService;
import com.project.vestiart.services.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stat")
public class StatController {

    private final IUserService IUserService;
    private final IIdeaService IIdeaService;

    public StatController(IUserService IUserService, IIdeaService IIdeaService) {
        this.IUserService = IUserService;
        this.IIdeaService = IIdeaService;
    }

    @Operation(summary = "retrieve the stats of the app")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stats", content = @Content(schema = @Schema(implementation = StatDTO.class)))
    })
    @GetMapping("/retrieve")
    public ResponseEntity<?> stat() {
        long nbUsers = IUserService.countUsers();
        long nbIdeas = IIdeaService.countIdeas();
        return ResponseEntity.ok(StatDTO.builder()
                .nbUsers(nbUsers)
                .nbCreations(nbIdeas)
                .build());
    }


}
