package com.tanidis.chatbot.controller;

import com.tanidis.chatbot.dto.ChatRequest;
import com.tanidis.chatbot.dto.ChatResponse;
import com.tanidis.chatbot.model.Conversation;
import com.tanidis.chatbot.model.Message;
import com.tanidis.chatbot.repository.ConversationRepo;
import com.tanidis.chatbot.repository.MessageRepo;
import com.tanidis.chatbot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ConversationRepo conversationRepo;
    private final MessageRepo messageRepo;

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        ChatResponse response = chatService.sendMessage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<Map<String, Object>>> getConversations() {
        List<Conversation> conversations = conversationRepo.findAll();
        List<Map<String, Object>> result = conversations.stream()
                .map(conv -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("id", conv.getId());
                    map.put("title", conv.getTitle());
                    map.put("createdAt", conv.getCreatedAt());
                    return map;
                })
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<List<Map<String, Object>>> getMessages(@PathVariable Long id) {
        List<Message> messages = messageRepo.findByConversationIdOrderByCreatedAtAsc(id);
        List<Map<String, Object>> result = messages.stream()
                .map(msg -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("id", msg.getId());
                    map.put("role", msg.getRole());
                    map.put("content", msg.getContent());
                    map.put("createdAt", msg.getCreatedAt());
                    return map;
                })
                .toList();
        return ResponseEntity.ok(result);
    }
}