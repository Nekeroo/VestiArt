package com.project.vestiart.controllers;

import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.models.input.RequestInput;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class OpenAIController {

    private final OpenAIService openAIService;
    private String imageFilesPath = "./output/";
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

    public String getImage(RequestInput input, String prompt) throws IOException, URISyntaxException {

            ImageResponse response = openAIService.createImage(prompt);

            String simplifyName = imageFilesPath + prompt
                    .substring(0, Math.min(prompt.length(), 20))
                    .toLowerCase()
                    .replace(",","")
                    .replace("'","")
                    .replace(" ","_")
                    + ".png" ;

            Image image = response.getResult().getOutput();

            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(image.getUrl());
            RequestEntity<Void> request = RequestEntity.get(uri).build();
            ResponseEntity<byte[]> imageResponse = restTemplate.exchange(request, byte[].class);

            byte[] imageBytes = imageResponse.getBody();

            BucketInfos bucketInfos = bucketService.uploadFileFromGeneration(input, imageBytes);

            return bucketInfos.getUrl();

        }

}
