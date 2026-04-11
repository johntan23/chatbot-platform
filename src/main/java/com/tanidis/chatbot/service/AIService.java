package com.tanidis.chatbot.service;

import com.tanidis.chatbot.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private final RestClient restClient;
    private final String apiKey;
    private final String baseUrl;
    private final String model;

    public AIService(
            RestClient restClient,
            @Value("${groq.api-key}") String apiKey,
            @Value("${groq.base-url}") String baseUrl,
            @Value("${groq.model}") String model) {
        this.restClient = restClient;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.model = model;
    }

    public String chat(List<Message> history, String systemPrompt, Double temperature) {
        List<Map<String, String>> messages = new ArrayList<>();

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

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.post()
                    .uri(baseUrl + "/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                return "Sorry, I could not get a response. Please try again.";
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");

            if (choices == null || choices.isEmpty()) {
                return "Sorry, I could not get a response. Please try again.";
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            if (message == null) {
                return "Sorry, I could not get a response. Please try again.";
            }

            return (String) message.get("content");

        } catch (RestClientException e) {
            return "Sorry, there was an error connecting to the AI service. Please try again.";
        }
    }
}