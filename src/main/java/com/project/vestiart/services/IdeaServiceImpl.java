package com.project.vestiart.services;

import com.project.vestiart.models.Idea;
import com.project.vestiart.models.Idea;
import com.project.vestiart.repositories.IdeaRepository;
import com.project.vestiart.services.interfaces.IdeaService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IdeaServiceImpl implements IdeaService {

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
        idea.setDateCreate(LocalDateTime.now());
        ideaRepository.save(idea);
    }

    public Optional<Idea> getIdeaByIdExterne(String idExterne){
        return ideaRepository.findByidExterne(idExterne);
    }

    public void removeIdea(Idea Idea) {
        ideaRepository.delete(Idea);
    }

    public List<Idea> retrieveLastIdea(int numberOfElements) {
        Pageable pageRequest = PageRequest.of(0, numberOfElements);
        List<Idea> Idea = ideaRepository.findAllByOrderByDateCreateDesc(pageRequest);
        return Idea;
    }

    public List<Idea> retrievePaginatedIdea(int page, int size) {
        List<Idea> Idea = new ArrayList<>();
        Iterable<Idea> allIdea = ideaRepository.findAll();
        int start = page * size;
        int end = Math.min(start + size, (int) ideaRepository.count());
        for (int i = start; i < end; i++) {
            Idea.add((Idea) allIdea);
        }
        return Idea;
    }
}
