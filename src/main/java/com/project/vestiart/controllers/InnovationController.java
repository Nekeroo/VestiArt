package com.project.vestiart.controllers;

import com.project.vestiart.enums.TypeEnum;
import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.models.Idea;
import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.input.RequestInput;
import com.project.vestiart.services.IdeaServiceImpl;
import com.project.vestiart.services.RequestInputService;
import com.project.vestiart.utils.PromptUtils;
import com.project.vestiart.utils.mappers.IdeaMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/innovation")
public class InnovationController {

    private final OpenAIController openAIController;
    private final IdeaServiceImpl ideaService;
    private final RequestInputService requestInputService;
    private final IdeaMapper ideaMapper;

    public InnovationController(OpenAIController openAIController, IdeaServiceImpl ideaService, RequestInputService requestInputService, IdeaMapper ideaMapper) {
        this.openAIController = openAIController;
        this.ideaService = ideaService;
        this.requestInputService = requestInputService;
        this.ideaMapper = ideaMapper;
    }

    public IdeaDTO createIdeaFromRequest(@RequestBody RequestInput input) throws IOException, URISyntaxException {

        String promptText = PromptUtils.formatPromptRequest(input.getPerson(), input.getReference(), input.getType());

        String resultFromTheIdea = openAIController.getChatResponse(promptText);

        String promptImage = PromptUtils.formatPromptImage(promptText);

        BucketInfos bucketInfos = openAIController.getImage(input, promptImage);

        Idea idea = Idea.builder()
                .image(bucketInfos.getUrl())
                .idExterne(bucketInfos.getIdExterne())
                .description(resultFromTheIdea)
                .title(input.getPerson() + " Collection")
                .tag1(input.getPerson())
                .tag2(input.getReference())
                .type(TypeEnum.findTypeEnumById(input.getType()))
                .build();

        idea = ideaService.saveIdea(idea);
        input.setIdea(idea);
        requestInputService.saveRequestInput(input);

        return ideaMapper.mapIdeaToIdeaDTO(idea);
    }

    @PostMapping("/create")
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
