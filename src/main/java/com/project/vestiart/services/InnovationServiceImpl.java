package com.project.vestiart.services;

import com.project.vestiart.input.RequestInputDTO;
import com.project.vestiart.enums.TypeEnum;
import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.models.Idea;
import com.project.vestiart.models.PdfInfos;
import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.models.RequestInput;
import com.project.vestiart.services.database.IdeaServiceImpl;
import com.project.vestiart.services.database.RequestInputService;
import com.project.vestiart.utils.PromptUtils;
import com.project.vestiart.utils.mappers.IdeaMapper;
import com.project.vestiart.utils.mappers.RequestInputMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class InnovationServiceImpl {

    private final OpenAIServiceImpl openAIService;
    private final IdeaServiceImpl ideaService;
    private final RequestInputService requestInputService;
    private final IdeaMapper ideaMapper;
    private final AsyncService asyncService;
    private final RequestInputMapper requestInputMapper;
    private final PdfServiceImpl pdfService;

    private static final Logger LOGGER = LoggerFactory.getLogger(InnovationServiceImpl.class);

    public InnovationServiceImpl(OpenAIServiceImpl openAIService,
                                 IdeaServiceImpl ideaService,
                                 RequestInputService requestInputService,
                                 IdeaMapper ideaMapper,
                                 AsyncService asyncService,
                                 RequestInputMapper requestInputMapper,
                                 PdfServiceImpl pdfService) {
        this.openAIService = openAIService;
        this.ideaService = ideaService;
        this.requestInputService = requestInputService;
        this.ideaMapper = ideaMapper;
        this.asyncService = asyncService;
        this.requestInputMapper = requestInputMapper;
        this.pdfService = pdfService;
    }

    public IdeaDTO createIdea(RequestInputDTO input) throws IOException, URISyntaxException {
        String promptText = buildPromptText(input);
        String resultFromTheIdea = fetchIdeaDescription(promptText);
        BucketInfos bucketInfos = fetchImage(promptText, input);

        Idea idea = buildIdeaEntity(input, resultFromTheIdea, bucketInfos);
        RequestInput requestInput = mapRequestInput(input, idea);

        persistIdeaAndRequestInput(idea, requestInput);
        enrichWithPdf(idea);

        return ideaMapper.mapIdeaToIdeaDTO(idea);
    }

    public List<IdeaDTO> createMultipleIdeas(List<RequestInputDTO> inputs) {
        return inputs.stream()
                .map(input -> asyncService.runAsync(() -> {
                    try {
                        return createIdea(input);
                    } catch (IOException | URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }))
                .map(CompletableFuture::join)
                .toList();
    }
    private String buildPromptText(RequestInputDTO input) {
        return PromptUtils.formatPromptRequest(
                input.getPerson(),
                input.getReference(),
                TypeEnum.findTypeEnumByType(input.getType()).getType()
        );
    }

    private String fetchIdeaDescription(String promptText) {
        LOGGER.info("Request Text");
        return openAIService.getMessageFromResponseOpenAi(promptText);
    }

    private BucketInfos fetchImage(String promptText, RequestInputDTO input) throws URISyntaxException {
        LOGGER.info("Request Image");
        String promptImage = PromptUtils.formatPromptImage(promptText);
        return openAIService.getImageFromResponseOpenAi(input, promptImage);
    }

    private Idea buildIdeaEntity(RequestInputDTO input, String description, BucketInfos bucketInfos) {
        return Idea.builder()
                .image(bucketInfos.getUrl())
                .idExterneImage(bucketInfos.getIdExterne())
                .description(description)
                .title(input.getPerson() + " Collection")
                .tag1(input.getPerson())
                .tag2(input.getReference())
                .type(TypeEnum.findTypeEnumByType(input.getType()))
                .build();
    }

    private RequestInput mapRequestInput(RequestInputDTO dto, Idea idea) {
        RequestInput requestInput = requestInputMapper.mapRequestInputDTOintiRequestInput(dto);
        requestInput.setIdea(idea);
        return requestInput;
    }

    private void persistIdeaAndRequestInput(Idea idea, RequestInput requestInput) {
        ideaService.saveIdea(idea);
        requestInputService.saveRequestInput(requestInput);
    }

    private void enrichWithPdf(Idea idea) throws IOException {
        PdfInfos pdfInfos = pdfService.generatePdf(idea);
        idea.setIdExternePdf(pdfInfos.getIdExternePdf());
        idea.setPdf(pdfInfos.getUrl());
        ideaService.updateIdea(idea);
    }
}
