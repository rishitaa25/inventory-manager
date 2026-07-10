package org.example.inventory_manager_beta1.MainApplicationClasses;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.example.inventory_manager_beta1.Entities.ChatMessage;
import org.example.inventory_manager_beta1.Repositories.ChatMessageRepository;
import org.example.inventory_manager_beta1.Entities.GroupChat;
import org.example.inventory_manager_beta1.Repositories.GroupChatRepository;
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

    private static GroupChatRepository groupRepo;

    @Autowired
    public void setGroupChatRepository(GroupChatRepository repo) {ChatWebSocket.groupRepo = repo;}

    // Manage sessions by room: Map<RoomName, Set<Sessions>>
    private static Map<String, CopyOnWriteArraySet<Session>> roomSessions = new Hashtable<>();
    /**
     * A Logger variable for writing to the server console
     */
    private final Logger logger = LoggerFactory.getLogger(ChatWebSocket.class);
    /**
     * A static ChatMessageRepository variable because WebSockets are not Spring-managed beans by default
     */
    // Static reference to repository because WebSockets are not Spring-managed beans by default
    private static ChatMessageRepository chatRepo;

    /**
     * A logic method that initialized the ChatMessageRepository variable
     * @param repo
     */
    @Autowired
    public void setChatMessageRepository(ChatMessageRepository repo) {
        ChatWebSocket.chatRepo = repo;
    }

    /**
     * A logic method for what to do when a user opens a WebSocket connection
     * @param session
     *  The WebSocket session created when the connection is opened
     * @param room
     *  The name of the chat room
     * @param username
     *  The username of the person who joined
     */
    @OnOpen
    public void onOpen(Session session,
                       @PathParam("room") String room,
                       @PathParam("username") String username) {

        logger.info("User " + username + " trying to join room: " + room);

        // Check if this room is a restricted group chat
        GroupChat group = groupRepo.findByRoomName(room).orElse(null);

        if (group != null && !group.isActive()) {
            try {
                session.getBasicRemote().sendText("CHAT CLOSED");
                session.close();
            } catch (IOException e) {
                logger.error("Error closing inactive chat session: " + e.getMessage());
            }
            return;
        }

        if (group != null) {
            boolean isMember = group.getMembers().stream()
                    .anyMatch(emp -> emp.getUserName().equals(username));

            if (!isMember) {
                try {
                    session.getBasicRemote().sendText("ACCESS DENIED");
                    session.close();
                } catch (IOException e) {
                    logger.error("Error closing session: " + e.getMessage());
                }
                return;
            }
        }

        // Allow join (either public OR valid member)
        roomSessions.computeIfAbsent(room, k -> new CopyOnWriteArraySet<>()).add(session);
        broadcastToRoom(room, "SYSTEM: " + username + " has joined the chat.");
    }

    /**
     * A logic method for what to do when a user sends a message in the chat room
     * @param session
     *  The WebSocket session the user is currently in
     * @param message
     *  The message the user sent
     * @param room
     *  The name of the chat room
     * @param username
     *  The username of the person who sent the message
     */
    @OnMessage
    public void onMessage(Session session, String message, @PathParam("room") String room, @PathParam("username") String username) {
        logger.info("Message in " + room + " from " + username + ": " + message);

        GroupChat group = groupRepo.findByRoomName(room).orElse(null);

        if (group != null && !group.isActive()) {
            try {
                session.getBasicRemote().sendText("CHAT CLOSED");
            } catch (IOException e) {
                logger.error("Error sending closed chat message: " + e.getMessage());
            }
            return;
        }
        // Save to Database
        ChatMessage msgEntity = new ChatMessage(username, room, message);
        chatRepo.save(msgEntity);

        // Broadcast to only the group
        broadcastToRoom(room, username + ": " + message);
    }

    /**
     * A logic method for what to do when a user closes the WebSocket connection
     * @param session
     *  The WebSocket session that was being used
     * @param room
     *  The name of the chat room
     * @param username
     *  The username of the person who disconnected
     */
    @OnClose
    public void onClose(Session session, @PathParam("room") String room, @PathParam("username") String username) {
        if (roomSessions.containsKey(room)) {
            roomSessions.get(room).remove(session);
        }
        broadcastToRoom(room, "SYSTEM: " + username + " has left.");
    }

    /**
     * Broadcasts a notification to all connected users in a room
     * @param room
     *  The name of the chat room
     * @param message
     *  The message being broadcasted
     */
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