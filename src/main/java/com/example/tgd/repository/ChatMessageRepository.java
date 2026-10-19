package com.example.tgd.repository;

import com.example.tgd.model.ChatMessageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessageDocument, String> {
    List<ChatMessageDocument> findBySessionIdOrderByCreatedAtAsc(String sessionId);
}