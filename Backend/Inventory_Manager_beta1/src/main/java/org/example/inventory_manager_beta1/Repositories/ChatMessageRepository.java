package org.example.inventory_manager_beta1.Repositories;

import org.example.inventory_manager_beta1.Entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * A repository containing all the custom database searches relevant to the ChatMessage class.
 * Allows retrieving chat history for specific rooms.
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    /**
     * A custom JpaRepository search method to search a database for all ChatMessages
     * in a specific room, ordered by timestamp in ascending order
     * @param room
     *  The name of the room to retrieve chat history for
     * @return
     *  A list of ChatMessages in the matching room, ordered by timestamp ascending
     */
    List<ChatMessage> findByRoomOrderByTimestampAsc(String room);
}