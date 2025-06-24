package com.project.vestiart.services.interfaces;

import com.project.vestiart.dto.RetrieveIdeaDTO;
import com.project.vestiart.models.Idea;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IIdeaService {

    List<Idea> retrieveAll();

    void saveIdea(Idea idea);

    Optional<Idea> getIdeaByIdExternePdf(String idExternePdf);

    void removeIdea(Idea Idea);

    List<Idea> retrieveLastIdea(int numberOfElements);

    List<Idea> retrievePaginatedIdea(int page, int size);

    Idea updateIdea(Idea idea);

    RetrieveIdeaDTO getIdeasAfterDynamic(int start, int size, String orderType);

    RetrieveIdeaDTO getIdeasFromIdUser(long idUser, int start, int size);

    RetrieveIdeaDTO getIdeaFromType(int start, int size, String type);

    long countIdeas();
}
