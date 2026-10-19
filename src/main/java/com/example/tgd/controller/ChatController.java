package com.example.tgd.controller;

import com.example.tgd.model.ChatMessageDocument;
import com.example.tgd.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(Map.of("response", chatService.sendMessage(payload)));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessageDocument>> getHistory(@RequestParam String sessionId) {
        return ResponseEntity.ok(chatService.getHistory(sessionId));
    }
}