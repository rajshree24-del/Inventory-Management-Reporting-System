package com.enterprise.inventory.service;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OpenAIService {

    private final OpenAiService service;

    public OpenAIService(@Value("${openai.api.key}") String apiKey) {
        service = new OpenAiService(apiKey);
    }

    public String askAI(String message) {

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4o-mini")
                .messages(List.of(
                        new ChatMessage("system",
                                "You are an AI assistant for an Inventory Management System."),
                        new ChatMessage("user", message)
                ))
                .maxTokens(200)
                .build();

        return service.createChatCompletion(request)
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}