package com.project.vestiart.controllers;

import com.itextpdf.text.DocumentException;
import com.project.vestiart.models.input.RequestInput;
import com.project.vestiart.services.OpenAIService;
import com.project.vestiart.services.PdfService;
import com.project.vestiart.utils.PromptUtils;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.model.Generation;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/openapi/ai")
public class OpenAIController {

    private final String imageFilesPath = "../output/image/" ;
    private final OpenAIService openAIService;
    private final PdfService pdfService;

    public OpenAIController(OpenAIService openAIService, PdfService pdfService) {
        this.openAIService = openAIService;
        this.pdfService = pdfService;
    }



    @GetMapping("/message")
    public String getChatResponse(@RequestBody RequestInput requestInput) throws DocumentException, FileNotFoundException {

        String prompt = PromptUtils.formatPromptRequest(requestInput.getPerson(), requestInput.getReference(), requestInput.getType());

        ChatResponse response = openAIService.createMessage(prompt);

        Generation generation = response.getResult() ;
        return pdfService.generatePdf(generation.getOutput().getText(), requestInput.getPerson());
    }

}
