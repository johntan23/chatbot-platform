package com.tanidis.chatbot.service;

import com.tanidis.chatbot.dto.ChatRequest;
import com.tanidis.chatbot.dto.ChatResponse;
import com.tanidis.chatbot.model.Conversation;
import com.tanidis.chatbot.model.Message;
import com.tanidis.chatbot.repository.ConversationRepo;
import com.tanidis.chatbot.repository.MessageRepo;
import com.tanidis.chatbot.security.PromptSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ConversationRepo conversationRepo;
    private final MessageRepo messageRepo;
    private final AIService aiService;
    private final PromptSanitizer promptSanitizer;

    private static final String DEFAULT_SYSTEM_PROMPT = "You are a helpful assistant.";
    private static final Double DEFAULT_TEMPERATURE = 0.7;

    public ChatResponse sendMessage(ChatRequest request) {
        String sanitized = promptSanitizer.sanitize(request.getMessage());

        Conversation conversation = conversationRepo
                .findById(request.getConversationId())
                .orElseGet(() -> {
                    Conversation newConv = new Conversation();
                    newConv.setTitle("New Conversation");
                    return conversationRepo.save(newConv);
                });

        Message userMessage = new Message();
        userMessage.setRole(Message.Role.USER);
        userMessage.setContent(sanitized);
        userMessage.setConversation(conversation);
        messageRepo.save(userMessage);

        List<Message> history = messageRepo
                .findByConversationIdOrderByCreatedAtAsc(conversation.getId());

        String aiResponse = aiService.chat(history, DEFAULT_SYSTEM_PROMPT, DEFAULT_TEMPERATURE);

        Message assistantMessage = new Message();
        assistantMessage.setRole(Message.Role.ASSISTANT);
        assistantMessage.setContent(aiResponse);
        assistantMessage.setConversation(conversation);
        messageRepo.save(assistantMessage);

        ChatResponse response = new ChatResponse();
        response.setConversationId(conversation.getId());
        response.setMessage(aiResponse);
        response.setRole("ASSISTANT");
        return response;
    }
}