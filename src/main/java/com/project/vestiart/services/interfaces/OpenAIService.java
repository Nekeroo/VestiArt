package com.project.vestiart.services.interfaces;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.image.ImageResponse;

public interface OpenAIService {

    ChatResponse createMessage(String prompt);

    ImageResponse createImage(String prompt);

}
