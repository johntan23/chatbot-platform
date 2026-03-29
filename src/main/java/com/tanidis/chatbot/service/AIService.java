package com.tanidis.chatbot.service;

import com.tanidis.chatbot.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private final RestClient restClient;

    @Value("${groq.api-key}")
    private String apiKey;

    @Value("${groq.base-url}")
    private String baseUrl;

    @Value("${groq.model}")
    private String model;

    public AIService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String chat(List<Message> history, String systemPrompt, Double temperature) {
        List<Map<String, String>> messages = new java.util.ArrayList<>();

        messages.add(Map.of("role", "system", "content", systemPrompt));

        for (Message msg : history) {
            messages.add(Map.of(
                    "role", msg.getRole().name().toLowerCase(),
                    "content", msg.getContent()
            ));
        }

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", messages,
                "temperature", temperature
        );

        Map response = restClient.post()
                .uri(baseUrl + "/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        List<Map> choices = (List<Map>) response.get("choices");
        Map firstChoice = choices.get(0);
        Map message = (Map) firstChoice.get("message");
        return (String) message.get("content");
    }
}