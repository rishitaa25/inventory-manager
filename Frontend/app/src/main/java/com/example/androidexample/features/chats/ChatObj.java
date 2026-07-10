package com.example.androidexample.features.chats;

import java.time.LocalDateTime;

public class ChatObj {
    /**
     * A Long variable representing the id of the chat message
     */

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

    private String content;
    /**
     * A LocalDateTime variable representing the timestamp of the message
     */
    private LocalDateTime timestamp;


    /**
     * INIT: The All Args constructor for creating a ChatMessage
     * @param sender
     *  The name of the message sender
     * @param room
     *  The name of the room the message was sent in
     * @param content
     *  The content of the message
     */
    public ChatObj(Long id,String sender, String room, String content,LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.room = room;
        this.content = content;
            this.timestamp = timestamp;

    }
    public Long getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getRoom() {
        return room;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }



}
