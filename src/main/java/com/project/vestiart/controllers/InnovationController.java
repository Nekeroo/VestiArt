package com.project.vestiart.controllers;

import com.project.vestiart.models.dto.IdeaDTO;
import com.project.vestiart.models.input.RequestInput;
import com.project.vestiart.utils.PromptUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/innovation")
public class InnovationController {

    private final OpenAIController openAIController;

    public InnovationController(OpenAIController openAIController) {
        this.openAIController = openAIController;
    }

    @PostMapping("/create")
    public IdeaDTO createIdeaFromRequest(@RequestBody RequestInput input) throws IOException, URISyntaxException {

        String prompt = PromptUtils.formatPromptRequest(input.getPerson(), input.getReference(), input.getType());

        String resultFromTheIdea = openAIController.getChatResponse(prompt);

        String image = openAIController.getImage(input, resultFromTheIdea);

        return IdeaDTO.builder()
                .image(image)
                .description(resultFromTheIdea)
                .title(input.getPerson() + " Collection")
                .build();
    }

    @PostMapping("/create/multiple")
    public List<IdeaDTO> createMultipleIdeasFromRequest(@RequestBody List<RequestInput> inputs) {

        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(inputs.size(), 10));

        List<CompletableFuture<IdeaDTO>> futures = inputs.stream()
                .map(input -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return createIdeaFromRequest(input);
                    } catch (IOException | URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }, executorService))
                .toList();

        List<IdeaDTO> ideaDTOS = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        executorService.shutdown();

        return ideaDTOS;
    }

}
