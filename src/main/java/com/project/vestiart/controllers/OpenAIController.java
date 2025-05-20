package com.project.vestiart.controllers;

import com.project.vestiart.models.input.RequestInput;
import com.project.vestiart.services.OpenAIService;
import com.project.vestiart.utils.PromptUtils;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.model.Generation;

@RestController
@RequestMapping("/openapi/ai")
public class OpenAIController {

    private final String imageFilesPath = "../output/image/" ;
    private final OpenAiChatModel chatModel;
    private final OpenAIService openAIService;

    public OpenAIController(OpenAiChatModel chatModel, OpenAIService openAIService) {
        this.chatModel = chatModel;
        this.openAIService = openAIService;
    }

    @GetMapping("/message")
    public String getChatResponse(@RequestBody RequestInput requestInput) {

        String prompt = PromptUtils.formatPromptRequest(requestInput.getPerson(), requestInput.getReference(), requestInput.getType());

        System.out.println(prompt);

        ChatResponse response = chatModel.call(
                new Prompt(
                        prompt,
                        OpenAiChatOptions.builder()
                                .model("gpt-4o-mini-2024-07-18")
                                .temperature(0.4)
                                .build()
                ));

        Generation generation = response.getResult() ;
        return generation.getOutput().getText() ;
    }

}
