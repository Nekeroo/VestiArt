package com.project.vestiart.services.interfaces;

import com.project.vestiart.models.Idea;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IdeaService {

    List<Idea> retrieveAll();

    void saveIdea(Idea idea);

    Optional<Idea> getIdeaByIdExterne(String idExterne);

    void removeIdea(Idea Idea);

    List<Idea> retrieveLastIdea(int numberOfElements);

    List<Idea> retrievePaginatedIdea(int page, int size);

    Idea updateIdea(Idea idea);


}
