package com.project.vestiart.controllers;

import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.input.RequestInputDTO;
import com.project.vestiart.services.InnovationServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/innovation")
public class InnovationController {

    private final InnovationServiceImpl innovationService;

    public InnovationController(InnovationServiceImpl innovationService) {
        this.innovationService = innovationService;
    }

    @PostMapping("/create")
    public List<IdeaDTO> createMultipleIdeasFromRequest(@RequestBody List<RequestInputDTO> inputs) {
        return innovationService.createMultipleIdeas(inputs);
    }
}
