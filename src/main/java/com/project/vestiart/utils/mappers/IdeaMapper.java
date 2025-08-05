package com.project.vestiart.utils.mappers;

import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.models.Idea;
import org.springframework.stereotype.Component;

@Component
public class IdeaMapper {

    public IdeaDTO mapIdeaToIdeaDTO(Idea idea) {

        return IdeaDTO.builder()
                .title(idea.getTitle())
                .description(idea.getDescription())
                .tag1(idea.getTag1())
                .tag2(idea.getTag2())
                .type(idea.getType())
                .image(idea.getImage())
                .pdf(idea.getPdf())
                .dateCreate(idea.getDateCreate())
                .build();
    }

    public Idea mapIdeaDTOToIdea(IdeaDTO ideaDTO) {

        return Idea.builder()
                .title(ideaDTO.title())
                .description(ideaDTO.description())
                .image(ideaDTO.image())
                .pdf(ideaDTO.pdf())
                .tag1(ideaDTO.tag1())
                .tag2(ideaDTO.tag2())
                .type(ideaDTO.type())
                .dateCreate(ideaDTO.dateCreate())
                .build();
    }
    
}


