package org.example.inventory_manager_beta1.repositories;

import org.example.inventory_manager_beta1.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for ChatMessage.
 * Allows retrieving chat history for specific rooms.
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomOrderByTimestampAsc(String room);
}
