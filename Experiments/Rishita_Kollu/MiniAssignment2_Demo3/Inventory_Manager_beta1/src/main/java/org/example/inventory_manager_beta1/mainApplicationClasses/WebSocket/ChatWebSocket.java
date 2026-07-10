package org.example.inventory_manager_beta1.mainApplicationClasses.WebSocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.example.inventory_manager_beta1.entities.ChatMessage;
import org.example.inventory_manager_beta1.repositories.ChatMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket Server for Group Chat.
 * URL Pattern- ws://localhost:8080/chat/Inventory/John
 */
@ServerEndpoint("/chat/{room}/{username}")
@Component
public class ChatWebSocket {

    // Manage sessions by room: Map<RoomName, Set<Sessions>>
    private static Map<String, CopyOnWriteArraySet<Session>> roomSessions = new Hashtable<>();
    private final Logger logger = LoggerFactory.getLogger(ChatWebSocket.class);

    // Static reference to repository because WebSockets are not Spring-managed beans by default
    private static ChatMessageRepository chatRepo;

    @Autowired
    public void setChatMessageRepository(ChatMessageRepository repo) {
        ChatWebSocket.chatRepo = repo;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("room") String room, @PathParam("username") String username) {
        logger.info("User " + username + " joined room: " + room);

        roomSessions.computeIfAbsent(room, k -> new CopyOnWriteArraySet<>()).add(session);
        broadcastToRoom(room, "SYSTEM: " + username + " has joined the chat.");
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("room") String room, @PathParam("username") String username) {
        logger.info("Message in " + room + " from " + username + ": " + message);

        // Save to Database
        ChatMessage msgEntity = new ChatMessage(username, room, message);
        chatRepo.save(msgEntity);

        // Broadcast to only the group
        broadcastToRoom(room, username + ": " + message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("room") String room, @PathParam("username") String username) {
        if (roomSessions.containsKey(room)) {
            roomSessions.get(room).remove(session);
        }
        broadcastToRoom(room, "SYSTEM: " + username + " has left.");
    }

    private void broadcastToRoom(String room, String message) {
        if (roomSessions.containsKey(room)) {
            roomSessions.get(room).forEach(session -> {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.error("Broadcast error: " + e.getMessage());
                }
            });
        }
    }
}