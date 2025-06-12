package com.project.vestiart.controllers;

import com.project.vestiart.models.Idea;
import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.models.Idea;
import com.project.vestiart.services.BucketService;
import com.project.vestiart.services.interfaces.IdeaService;
import com.project.vestiart.utils.mappers.IdeaMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/idea")
public class IdeaController {

    private final IdeaMapper ideaMapper;
    private final BucketService bucketService;
    private final IdeaService ideaService;

    public IdeaController(IdeaMapper ideaMapper, BucketService bucketService, IdeaService ideaService) {
        this.ideaService = ideaService;
        this.ideaMapper = ideaMapper;
        this.bucketService = bucketService;
    }

    @GetMapping("/retrieve/all")
    public List<IdeaDTO> retrieveAllIdea() {
        List<Idea> ideaList =  ideaService.retrieveAll();

        List<IdeaDTO> resultList = new ArrayList<>();

        for (Idea bi : ideaList) {
            resultList.add(ideaMapper.mapIdeaToIdeaDTO(bi));
        }

        return resultList;
    }

    @GetMapping("/retrieve/{uid}")
    public ResponseEntity<?> retrieIdeaByUid(@PathVariable String uid) {
        Optional<Idea> idea = ideaService.getIdeaByIdExterne(uid);

        if (idea.isEmpty()) {
            return ResponseEntity.badRequest().body("Idea not found");
        }

        return ResponseEntity.ok(idea.get());
    }

    @PostMapping("/add")
    public IdeaDTO addIdea(@RequestBody IdeaDTO IdeaDTO) {
        Idea idea = ideaMapper.mapIdeaDTOToIdea(IdeaDTO);
        ideaService.saveIdea(idea);
        return IdeaDTO;
    }

    @DeleteMapping("/delete/{uid}")
    public ResponseEntity<?> remove(@PathVariable String uid) {
        Optional<Idea> idea = ideaService.getIdeaByIdExterne(uid);
        if (idea.isPresent()) {
            ideaService.removeIdea(idea.get());
            bucketService.deleteDocumentFromTheBucket(uid);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().body("Idea not found");
        }
    }

    @GetMapping("/retrieve/last/{numberOfElements}")
    public List<IdeaDTO> retrieveLastIdea(@PathVariable int numberOfElements) {
        List<Idea> ideaList = ideaService.retrieveLastIdea(numberOfElements);
        List<IdeaDTO> resultList = new ArrayList<>();

        for (Idea bi : ideaList) {
            resultList.add(ideaMapper.mapIdeaToIdeaDTO(bi));
        }

        return resultList;
    }

    @GetMapping("/retrieve/{page}/{size}")
    public ResponseEntity<List<IdeaDTO>> retrievePaginatedIdea(@PathVariable int page, @PathVariable int size) {
        List<Idea> Idea = ideaService.retrievePaginatedIdea(page, size);
        List<IdeaDTO> resultList = new ArrayList<>();

        for (Idea bi : Idea) {
            resultList.add(ideaMapper.mapIdeaToIdeaDTO(bi));
        }

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

}
