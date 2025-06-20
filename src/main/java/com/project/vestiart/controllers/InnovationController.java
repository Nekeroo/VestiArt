package com.project.vestiart.controllers;

import com.project.vestiart.dto.RequestInputDTO;
import com.project.vestiart.enums.TypeEnum;
import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.models.Idea;
import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.input.RequestInput;
import com.project.vestiart.services.AsyncService;
import com.project.vestiart.services.IdeaServiceImpl;
import com.project.vestiart.services.OpenAIServiceImpl;
import com.project.vestiart.services.RequestInputService;
import com.project.vestiart.utils.PromptUtils;
import com.project.vestiart.utils.mappers.IdeaMapper;
import com.project.vestiart.utils.mappers.RequestInputMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/innovation")
public class InnovationController {

    private final OpenAIServiceImpl openAIService;
    private final IdeaServiceImpl ideaService;
    private final RequestInputService requestInputService;
    private final IdeaMapper ideaMapper;
    private final AsyncService asyncService;
    private final RequestInputMapper requestInputMapper;

    private final PdfController pdfController;

    private static final Logger LOGGER = LoggerFactory.getLogger(InnovationController.class);

    public InnovationController(OpenAIServiceImpl openAIService, IdeaServiceImpl ideaService, RequestInputService requestInputService, IdeaMapper ideaMapper, AsyncService asyncService, RequestInputMapper requestInputMapper, PdfController pdfController) {
        this.openAIService = openAIService;
        this.ideaService = ideaService;
        this.requestInputService = requestInputService;
        this.ideaMapper = ideaMapper;
        this.asyncService = asyncService;
        this.requestInputMapper = requestInputMapper;
        this.pdfController = pdfController;
    }

    public IdeaDTO createIdeaFromRequest(@RequestBody RequestInputDTO input) throws IOException, URISyntaxException {

        String promptText = PromptUtils.formatPromptRequest(input.getPerson(), input.getReference(), TypeEnum.findTypeEnumByType(input.getType()).getType());

        LOGGER.info("Request Text");
        String resultFromTheIdea = openAIService.getMessageFromResponseOpenAi(promptText);

        String promptImage = PromptUtils.formatPromptImage(promptText);

        LOGGER.info("Request Image");
        BucketInfos bucketInfos = openAIService.getImageFromResponseOpenAi(input, promptImage);

        Idea idea = Idea.builder()
                .image(bucketInfos.getUrl())
                .idExterneImage(bucketInfos.getIdExterne())
                .description(resultFromTheIdea)
                .title(input.getPerson() + " Collection")
                .tag1(input.getPerson())
                .tag2(input.getReference())
                .type(TypeEnum.findTypeEnumByType(input.getType()))
                .build();

        ideaService.saveIdea(idea);
        RequestInput inputMapped = requestInputMapper.mapRequestInputDTOintiRequestInput(input);
        inputMapped.setIdea(idea);
        requestInputService.saveRequestInput(inputMapped);

        return ideaMapper.mapIdeaToIdeaDTO(idea);
    }

    @PostMapping("/create")
    public List<IdeaDTO> createMultipleIdeasFromRequest(@RequestBody List<RequestInputDTO> inputs) {

        List<CompletableFuture<IdeaDTO>> futures = inputs.stream()
                .map(input -> asyncService.runAsync(() -> {
                    try {
                        return createIdeaFromRequest(input);
                    } catch (IOException | URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }))
                .toList();

        // TODO : Need to use GeneratePDF during the Idea creation

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

}
