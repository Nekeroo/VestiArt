package com.project.vestiart.services;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class OpenAIServiceImpl implements OpenAIService{

    private final OpenAiChatModel chatModel;

    public OpenAIServiceImpl(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
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


}
