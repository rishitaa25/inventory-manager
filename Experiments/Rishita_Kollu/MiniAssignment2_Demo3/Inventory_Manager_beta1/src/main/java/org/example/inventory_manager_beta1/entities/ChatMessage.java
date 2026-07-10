package org.example.inventory_manager_beta1.entities;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;    // Username of the sender
    private String room;      // The group/department (ex: "WAREHOUSE", "LOGISTICS")

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime timestamp;

    public ChatMessage() {}

    public ChatMessage(String sender, String room, String content) {
        this.sender = sender;
        this.room = room;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
}
