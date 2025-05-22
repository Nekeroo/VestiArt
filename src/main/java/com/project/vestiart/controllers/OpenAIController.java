package com.project.vestiart.controllers;

import com.itextpdf.text.DocumentException;
import com.project.vestiart.models.input.RequestInput;
import com.project.vestiart.services.interfaces.OpenAIService;
import com.project.vestiart.services.interfaces.PdfService;
import com.project.vestiart.utils.PromptUtils;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.model.Generation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

@Controller
public class OpenAIController {

    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    public String getChatResponse(String prompt) {

        ChatResponse response = openAIService.createMessage(prompt);

        Generation generation = response.getResult() ;
        return generation.getOutput().getText();
    }

    public byte[] getImage(String prompt) throws IOException {

            ImageResponse response = openAIService.getImage(prompt);

            String imageUrl = response.getResult().getOutput().getUrl(); // à adapter

            try (InputStream in = new URL(imageUrl).openStream()) {
                return in.readAllBytes();
            }

    }

}
