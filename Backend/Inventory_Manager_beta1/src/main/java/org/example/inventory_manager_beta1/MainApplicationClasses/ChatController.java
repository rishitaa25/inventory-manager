package org.example.inventory_manager_beta1.MainApplicationClasses;

import org.example.inventory_manager_beta1.Entities.ChatMessage;
import org.example.inventory_manager_beta1.Repositories.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * This class provides API endpoints to interact with the chat database.
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
    /**
     * The auto-injected ChatMessageRepository for handling custom chat message searches
     */
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    /**
     * READ: Retrieves the message history for a specific room.
     * Use Case is When a user joins 'Logistics', the frontend calls this to show old messages.
     * @param room The name of the chat room (ex: Inventory)
     * @return List of messages ordered by time
     */
    @GetMapping("/history/{room}")
    public List<ChatMessage> getChatHistory(@PathVariable String room) {
        return chatMessageRepository.findByRoomOrderByTimestampAsc(room);
    }

    /**
     * DELETE: Clears chat history for a specific room.
     * Use Case is Admin cleanup or testing.
     * @param room The name of the chat room to clear
     * @return Confirmation message
     */
    @DeleteMapping("/history/clear/{room}")
    public String clearChatHistory(@PathVariable String room) {
        List<ChatMessage> messages = chatMessageRepository.findByRoomOrderByTimestampAsc(room);
        chatMessageRepository.deleteAll(messages);
        return "Chat history for " + room + " has been cleared.";
    }
}
