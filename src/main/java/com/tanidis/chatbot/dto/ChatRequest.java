package com.tanidis.chatbot.dto;

import lombok.Data;

@Data
public class ChatRequest {

    private Long conversationId;
    private String message;
}