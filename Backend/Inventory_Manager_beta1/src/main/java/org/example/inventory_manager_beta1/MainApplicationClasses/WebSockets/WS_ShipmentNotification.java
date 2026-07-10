package org.example.inventory_manager_beta1.MainApplicationClasses.WebSockets;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * A robust Web Socket class designed to handle the web socket notifications related to the Inventory Manager
 */
@ServerEndpoint("/shipments/{userName}")
@Component
public class WS_ShipmentNotification {
    /**
     * A Map variable linking active WebSocket Sessions to their associated usernames
     */
    private static final Map<Session, String> sessionUserNameMap = new Hashtable<>();
    /**
     * A Map variable linking active usernames to their associated WebSocket Sessions
     */
    private static final Map<String, Session> userNameSessionMap = new Hashtable<>();
    /**
     * A Logger variable for writing to the server console
     */
    private final Logger logger = LoggerFactory.getLogger(WS_ShipmentNotification.class);

    /**
     * A logic method for what to do when a user opens a WebSocket connection
     * @param session
     *  The WebSocket session created when the connection is opened
     * @param userName
     *  The username of the person who opened the connection
     * @throws IOException
     *  IOException if the user is already connected to this WebSocket session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) throws IOException {
        logger.info("[onOpen] " + userName);
        sessionUserNameMap.put(session, userName);
        userNameSessionMap.put(userName, session);
        logger.info(userName + " connected to shipment notifications");
    }

    /**
     * A logic method for what to do when a user closes the WebSocket connection
     * @param session
     *  The WebSocket session that was being used
     */
    @OnClose
    public void onClose(Session session) {
        String username = sessionUserNameMap.get(session);
        sessionUserNameMap.remove(session);
        userNameSessionMap.remove(username);
        logger.info("[onClose] " + username + " disconnected");
    }

    /**
     * A logic method for what to do if there is an error regarding WebSocket communication
     * @param session
     *  The WebSocket session being used
     * @param throwable
     *  The throwable Error message that was made
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        String username = sessionUserNameMap.get(session);
        logger.info("[onError] " + username+ ": " + throwable.getMessage());
    }

    /**
     * Sends a notification to a specific user
     * @param userName
     *  The userName of the recipient
     * @param message
     *  The notification message
     */
    public void notifyUser(String userName, String message) {
        try {
            if (userNameSessionMap.containsKey(userName)) {
                userNameSessionMap.get(userName).getBasicRemote().sendText(message);
            } else {
                logger.info("[notifyUser] " + userName + " is not connected");
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
        sessionUserNameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }
}