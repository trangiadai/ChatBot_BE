package com.example.tgd.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "chat_messages")
public class ChatMessageDocument {
	@Id
	private String id;
	private String sessionId;
	private String sender; // "USER" or "ASSISTANT"
	private String message;
	private LocalDateTime createdAt = LocalDateTime.now();

	public ChatMessageDocument() {
	}

	public ChatMessageDocument(String sessionId, String sender, String message) {
		this.sessionId = sessionId;
		this.sender = sender;
		this.message = message;
	}

	// Getters and Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}