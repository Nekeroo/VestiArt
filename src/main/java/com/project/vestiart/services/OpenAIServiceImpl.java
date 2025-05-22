package com.project.vestiart.services;

import com.project.vestiart.services.interfaces.OpenAIService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final OpenAiChatModel chatModel;
    private final OpenAiImageModel imageModel;

    public OpenAIServiceImpl(OpenAiChatModel chatModel, OpenAiImageModel imageModel) {
        this.chatModel = chatModel;
        this.imageModel = imageModel;
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

    public ImageResponse createImage(String prompt) {
        return imageModel.call(
                new ImagePrompt(prompt,
                        OpenAiImageOptions.builder()
                                .N(1)
                                .model("dall-e-2")
                                .height(1024)
                                .width(1024).build())
        );
    }
}
