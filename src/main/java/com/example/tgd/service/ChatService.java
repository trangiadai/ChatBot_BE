package com.example.tgd.service;

import com.example.tgd.model.ChatMessageDocument;
import com.example.tgd.repository.ChatMessageRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatClient.Builder chatClientBuilder, ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;

        // In-memory sliding window cache for active AI context
        ChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(20).build();

        // Build the Spring AI ChatClient with system persona instructions and memory advisor
        this.chatClient = chatClientBuilder
                .defaultSystem("You are a customer support agent for our e-commerce store. " +
                        "You ONLY answer questions related to store orders, products, and shipping. " +
                        "If a user asks about any other topic, give a short refusal: " +
                        "'I can only assist with customer support inquiries like orders and products.'")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String sendMessage(Map<String, String> payload) {
    	String sessionId = payload.get("sessionId");
        String userMessage = payload.get("message");
    	
        // 1. Save user message to MongoDB log
        chatMessageRepository.save(new ChatMessageDocument(sessionId, "USER", userMessage));

        // 2. Query local Ollama model (passing session ID context using ChatMemory.CONVERSATION_ID)
        String aiResponse = chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, sessionId))
                .user(userMessage)
                .call()
                .content();

        // 3. Save assistant reply to MongoDB log
        chatMessageRepository.save(new ChatMessageDocument(sessionId, "ASSISTANT", aiResponse));

        return aiResponse;
    }

    public List<ChatMessageDocument> getHistory(String sessionId) {
        return chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
    }
}