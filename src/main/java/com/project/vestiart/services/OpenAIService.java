package com.project.vestiart.services;

import org.springframework.ai.chat.model.ChatResponse;

public interface OpenAIService {

    ChatResponse createMessage(String prompt);

}
