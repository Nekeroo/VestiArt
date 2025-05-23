package com.project.vestiart.services;

import com.project.vestiart.models.Idea;
import com.project.vestiart.models.dto.IdeaDTO;
import com.project.vestiart.repositories.IdeaRepository;
import jakarta.persistence.Id;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IdeaServiceImpl {

    private final IdeaRepository ideaRepository;

    public IdeaServiceImpl(IdeaRepository ideaRepository) {
        this.ideaRepository = ideaRepository;
    }

    public List<Idea> retrieveAll() {
        Iterable<Idea> ideaList = ideaRepository.findAll();

        List<Idea> ideas    = new ArrayList<>();

        for (Idea idea : ideaList) {
                ideas.add(idea);
        }

        return ideas;
    }

    public void saveIdea(Idea idea) {
        ideaRepository.save(idea);
    }

}
