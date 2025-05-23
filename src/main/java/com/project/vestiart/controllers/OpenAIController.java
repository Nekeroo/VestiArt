package com.project.vestiart.controllers;

import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.input.RequestInput;
import com.project.vestiart.services.BucketService;
import com.project.vestiart.services.interfaces.OpenAIService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageResponse;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ai.chat.model.Generation;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class OpenAIController {

    private final OpenAIService openAIService;
    private final BucketService bucketService;

    public OpenAIController(OpenAIService openAIService, BucketService bucketService) {
        this.openAIService = openAIService;
        this.bucketService = bucketService;
    }

    public String getChatResponse(String prompt) {

        ChatResponse response = openAIService.createMessage(prompt);

        Generation generation = response.getResult() ;
        return generation.getOutput().getText();
    }

    public BucketInfos getImage(RequestInput input, String prompt) throws URISyntaxException {

            ImageResponse response = openAIService.createImage(prompt);

            Image image = response.getResult().getOutput();

            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(image.getUrl());
            RequestEntity<Void> request = RequestEntity.get(uri).build();
            ResponseEntity<byte[]> imageResponse = restTemplate.exchange(request, byte[].class);

            byte[] imageBytes = imageResponse.getBody();

            return bucketService.uploadFileFromGeneration(input.getPerson(), input.getReference(), imageBytes, "image");

        }

}
