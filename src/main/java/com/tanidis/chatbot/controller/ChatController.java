package com.tanidis.chatbot.controller;

import com.tanidis.chatbot.dto.ChatRequest;
import com.tanidis.chatbot.dto.ChatResponse;
import com.tanidis.chatbot.dto.ConversationDTO;
import com.tanidis.chatbot.dto.MessageDTO;
import com.tanidis.chatbot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        return ResponseEntity.ok(chatService.sendMessage(request));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDTO>> getConversations() {
        return ResponseEntity.ok(chatService.getConversations());
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.getMessages(id));
    }
}