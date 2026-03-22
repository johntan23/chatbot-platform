package com.tanidis.chatbot.dto;

import lombok.Data;

@Data
public class ChatResponse {

    private Long conversationId;
    private String message;
    private String role;
}