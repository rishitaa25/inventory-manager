package org.example.inventory_manager_beta1.mainApplicationClasses.WebSocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@ServerEndpoint("/shipments/{username}")
@Component
public class WS_ShipmentNotification {

    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(WS_ShipmentNotification.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        logger.info("[onOpen] " + username);

        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Already connected");
            session.close();
        } else {
            sessionUsernameMap.put(session, username);
            usernameSessionMap.put(username, session);
            logger.info(username + " connected to shipment notifications");
        }
    }

    @OnClose
    public void onClose(Session session) {
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        logger.info("[onClose] " + username + " disconnected");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        String username = sessionUsernameMap.get(session);
        logger.info("[onError] " + username + ": " + throwable.getMessage());
    }

    /**
     * Sends a notification to a specific user
     * @param username
     *  The username of the recipient
     * @param message
     *  The notification message
     */
    public void notifyUser(String username, String message) {
        try {
            if (usernameSessionMap.containsKey(username)) {
                usernameSessionMap.get(username).getBasicRemote().sendText(message);
            } else {
                logger.info("[notifyUser] " + username + " is not connected");
            }
        } catch (IOException e) {
            logger.info("[notifyUser Exception] " + e.getMessage());
        }
    }

    /**
     * Broadcasts a notification to all connected users
     * @param message
     *  The notification message
     */
    public void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }
}