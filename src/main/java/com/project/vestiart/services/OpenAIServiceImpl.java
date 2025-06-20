package com.project.vestiart.services;

import com.project.vestiart.dto.RequestInputDTO;
import com.project.vestiart.input.RequestInput;
import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.services.interfaces.OpenAIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final OpenAiChatModel chatModel;
    private final OpenAiImageModel imageModel;
    private final BucketService bucketService;
    private static final Logger LOGGER =  LoggerFactory.getLogger(OpenAIServiceImpl.class);

    public OpenAIServiceImpl(OpenAiChatModel chatModel, OpenAiImageModel imageModel, BucketService bucketService) {
        this.chatModel = chatModel;
        this.imageModel = imageModel;
        this.bucketService = bucketService;
    }

    public ChatResponse createMessage(String prompt) {;
        return chatModel.call(
                new Prompt(
                        prompt,
                        OpenAiChatOptions.builder()
                                .model("gpt-4o-mini-2024-07-18")
                                .temperature(0.4)
                                .build()
                ));
    }

    public String getMessageFromResponseOpenAi(String prompt) {

        ChatResponse response = this.createMessage(prompt);

        Generation generation = response.getResult() ;
        return generation.getOutput().getText();

    }

    public ImageResponse createImage(String prompt) {
        LOGGER.info("Creating image with prompt {}", prompt);
        return imageModel.call(
                new ImagePrompt(prompt,
                        OpenAiImageOptions.builder()
                                .N(1)
                                .model("dall-e-3")
                                .height(1024)
                                .width(1024).build())
        );
    }

    public BucketInfos getImageFromResponseOpenAi(RequestInputDTO input, String prompt) throws URISyntaxException {

        ImageResponse response = this.createImage(prompt);
        LOGGER.info("Image {}", response);

        Image image = response.getResult().getOutput();

        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(image.getUrl());
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        ResponseEntity<byte[]> imageResponse = restTemplate.exchange(request, byte[].class);

        byte[] imageBytes = imageResponse.getBody();

        return bucketService.uploadFileFromGeneration(input.getPerson(), input.getReference(), imageBytes, ".png");

    }
}
