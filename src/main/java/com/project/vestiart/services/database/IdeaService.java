package com.project.vestiart.services.database;

import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.dto.RetrieveIdeaDTO;
import com.project.vestiart.enums.TypeEnum;
import com.project.vestiart.models.Idea;
import com.project.vestiart.repositories.IIdeaRepository;
import com.project.vestiart.services.interfaces.IIdeaService;
import com.project.vestiart.utils.mappers.IdeaMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IdeaService implements IIdeaService {

    @PersistenceContext
    private EntityManager em;
    private final IIdeaRepository iidea;
    private final IdeaMapper ideaMapper;
    private final FileService fileService;

    public IdeaService(IIdeaRepository iidea, IdeaMapper ideaMapper, FileService fileService) {
        this.iidea = iidea;
        this.ideaMapper = ideaMapper;
        this.fileService = fileService;
    }

    public List<Idea> retrieveAll() {
        Iterable<Idea> ideaList = iidea.findAll();

        List<Idea> ideas    = new ArrayList<>();

        for (Idea idea : ideaList) {
            ideas.add(idea);
        }

        return ideas;
    }

    public void saveIdea(Idea idea) {
        idea.setDateCreate(LocalDateTime.now());
        fileService.saveFile(idea.getImage());
        iidea.save(idea);
    }

    public Idea updateIdea(Idea idea) {
        Idea ideaStored = iidea.findById(idea.getId()).get();

        ideaStored.setPdf(idea.getPdf());

        fileService.saveFile(idea.getPdf());
        iidea.save(ideaStored);

        return ideaStored;
    }

    public Optional<Idea> getIdeaFromId(String id){
        return iidea.findById(Long.parseLong(id));
    }

    public void removeIdea(Idea Idea) {
        iidea.delete(Idea);
    }

    public List<Idea> retrieveLastIdea(int numberOfElements) {
        Pageable pageRequest = PageRequest.of(0, numberOfElements);
        List<Idea> Idea = iidea.findAllByOrderByDateCreateDesc(pageRequest);
        return Idea;
    }

    public List<Idea> retrievePaginatedIdea(int page, int size) {
        List<Idea> Idea = new ArrayList<>();
        Iterable<Idea> allIdea = iidea.findAll();
        int start = page * size;
        int end = Math.min(start + size, (int) iidea.count());
        for (int i = start; i < end; i++) {
            Idea.add((Idea) allIdea);
        }
        return Idea;
    }

    public RetrieveIdeaDTO getIdeasAfterDynamic(int start, int size, String orderType) {
        String jpql = "SELECT i FROM Idea i ORDER BY i." + orderType + " DESC";
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


    public RetrieveIdeaDTO getIdeaFromType(int start, int size, String type) {
        String jpql = "SELECT i FROM Idea i WHERE i.type = :type";

        if (TypeEnum.findTypeEnumByType(type) != null) {
            List<Idea> ideas = em.createQuery(jpql, Idea.class)
                    .setParameter("type", TypeEnum.findTypeEnumByType(type))
                    .setFirstResult(start)
                    .setMaxResults(size)
                    .getResultList();

            List<IdeaDTO> ideasDTO = ideas.stream().map(ideaMapper::mapIdeaToIdeaDTO).collect(Collectors.toList());

            return RetrieveIdeaDTO.builder()
                    .ideas(ideasDTO)
                    .nextKey(start + ideas.size())
                    .build();
        }
        else {
            return null;
        }
    }

    public long countIdeas() {
        return iidea.count();
    }

}
