package org.example.inventory_manager_beta1.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Entity class to store chat messages in the database.
 */
@Entity
@Table(name = "chat_messages")
@Getter
@Setter
public class ChatMessage {
    /**
     * A Long variable representing the id of the chat message
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * A String variable representing the username of the message sender
     */
    private String sender;
    /**
     * A String variable representing the name of the chat room
     */
    private String room;
    /**
     * A String variable representing the message
     */
    @Column(columnDefinition = "TEXT")
    private String content;
    /**
     * A LocalDateTime variable representing the timestamp of the message
     */
    private LocalDateTime timestamp;

    /**
     * INIT: The No Args constructor required by SpringBoot
     */
    public ChatMessage() {}

    /**
     * INIT: The All Args constructor for creating a ChatMessage
     * @param sender
     *  The name of the message sender
     * @param room
     *  The name of the room the message was sent in
     * @param content
     *  The content of the message
     */
    public ChatMessage(String sender, String room, String content) {
        this.sender = sender;
        this.room = room;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
}
