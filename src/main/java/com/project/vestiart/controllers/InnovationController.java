package com.project.vestiart.controllers;

import com.itextpdf.text.DocumentException;
import com.project.vestiart.models.dto.IdeaDTO;
import com.project.vestiart.models.input.RequestInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@Slf4j
@RestController("/innovation")
public class InnovationController {

    private final OpenAIController openAIController;

    public InnovationController(OpenAIController openAIController) {
        this.openAIController = openAIController;
    }

    @PostMapping("/create")
    public IdeaDTO createIdeaFromRequest(@RequestBody RequestInput input) throws DocumentException, FileNotFoundException {

        // First Step, we call OpenAI for getting the Idea from the form
        String resultFromTheIdea = openAIController.getChatResponse(input);

        byte[] image = new byte[0];
        // TODO : Récupérer image depuis la réponse précédente + la transformer en byteArray
        return IdeaDTO.builder()
                .image(image)
                .description(resultFromTheIdea)
                .title(input.getPerson() + " Collection")
                .build();
    }

}
