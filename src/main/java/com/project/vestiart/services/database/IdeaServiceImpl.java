package com.project.vestiart.services.database;

import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.dto.RetrieveIdeaDTO;
import com.project.vestiart.models.Idea;
import com.project.vestiart.models.User;
import com.project.vestiart.repositories.IdeaRepository;
import com.project.vestiart.services.interfaces.IdeaService;
import com.project.vestiart.utils.mappers.IdeaMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IdeaServiceImpl implements IdeaService {

    @PersistenceContext
    private EntityManager em;
    private final IdeaRepository ideaRepository;
    private final IdeaMapper ideaMapper;

    public IdeaServiceImpl(IdeaRepository ideaRepository, IdeaMapper ideaMapper) {
        this.ideaRepository = ideaRepository;
        this.ideaMapper = ideaMapper;
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

    public Idea updateIdea(Idea idea) {
        Idea ideaStored = ideaRepository.findById(idea.getId()).get();

        ideaStored.setIdExternePdf(idea.getIdExternePdf());
        ideaStored.setPdf(idea.getPdf());

        ideaRepository.save(ideaStored);

        return ideaStored;
    }

    public Optional<Idea> getIdeaByIdExterne(String idExterne){
        return ideaRepository.findByidExterneImage(idExterne);
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

    public RetrieveIdeaDTO getIdeasAfterDynamic(int start, int size, String orderType) {
        String jpql = "SELECT i FROM Idea i ORDER BY i." + orderType;
        List<Idea> ideas = em.createQuery(jpql, Idea.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();

        List<IdeaDTO> ideasDTO = ideas.stream().map(ideaMapper::mapIdeaToIdeaDTO).toList();

        return RetrieveIdeaDTO.builder()
                .ideas(ideasDTO)
                .nextKey(start + ideas.size())
                .build();
    }

    public RetrieveIdeaDTO getIdeasFromIdUser(long idUser, int start, int size) {
        String jpql = "SELECT i FROM Idea i WHERE i.user.id = " + idUser;
        List<Idea> ideas = em.createQuery(jpql, Idea.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();

        List<IdeaDTO> ideasDTO = ideas.stream().map(ideaMapper::mapIdeaToIdeaDTO).collect(Collectors.toList());

        return RetrieveIdeaDTO.builder()
                .ideas(ideasDTO)
                .nextKey(start + ideas.size())
                .build();
    }

}
