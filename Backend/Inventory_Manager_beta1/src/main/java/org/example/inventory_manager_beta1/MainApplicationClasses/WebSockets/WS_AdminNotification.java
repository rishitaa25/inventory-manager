package org.example.inventory_manager_beta1.MainApplicationClasses.WebSockets;

import org.example.inventory_manager_beta1.Services.NotificationService;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * A WebSocket endpoint dedicated to pushing real-time notifications to connected admins.
 *
 * <p>Admins connect to this endpoint after login. When an employee or driver submits
 * an update request, {@link NotificationService}
 * calls {@link #broadcastToAdmins(String)} to instantly alert all connected admins
 * without them needing to poll the server.
 *
 * <p>Connection URL: {@code ws://current-server:8080/admin-notifications/{userName}}
 */
@ServerEndpoint("/admin-notifications/{userName}")
@Component
public class WS_AdminNotification {

    private static final Map<Session, String> sessionUserNameMap = new Hashtable<>();
    private static final Map<String, Session> userNameSessionMap = new Hashtable<>();
    private final Logger logger = LoggerFactory.getLogger(WS_AdminNotification.class);

    /**
     * Called when an admin opens a WebSocket connection.
     * Registers the session so notifications can be pushed to this admin.
     * Rejects the connection if the username is already connected.
     * @param session
     *  The WebSocket session created when the connection is opened
     * @param userName
     *  The username of the admin who opened the connection
     * @throws IOException
     *  If sending the rejection message fails
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) throws IOException {
        logger.info("[onOpen] " + userName);
        sessionUserNameMap.put(session, userName);
        userNameSessionMap.put(userName, session);
        logger.info(userName + " connected to Admin notifications");
    }

    /**
     * Called when an admin closes the WebSocket connection.
     * Removes the session from both maps.
     * @param session
     *  The WebSocket session that was closed
     */
    @OnClose
    public void onClose(Session session) {
        String userName = sessionUserNameMap.get(session);
        sessionUserNameMap.remove(session);
        userNameSessionMap.remove(userName);
        logger.info("[onClose] Admin disconnected: " + userName);
    }

    /**
     * Called when a WebSocket error occurs.
     * Logs the error with the associated username for debugging.
     * @param session
     *  The WebSocket session where the error occurred
     * @param throwable
     *  The error that was thrown
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        String userName = sessionUserNameMap.get(session);
        logger.info("[onError] " + userName + ": " + throwable.getMessage());
    }

    /**
     * Sends a notification to a specific admin by username.
     * If the admin is not connected, the message is silently dropped and logged.
     * @param userName
     *  The username of the admin to notify
     * @param message
     *  The notification message to send
     */
    public void notifyAdmin(String userName, String message) {
        try {
            if (userNameSessionMap.containsKey(userName)) {
                userNameSessionMap.get(userName).getBasicRemote().sendText(message);
                logger.info("[notifyAdmin] Sent to " + userName + ": " + message);
            } else {
                logger.info("[notifyAdmin] " + userName + " is not connected. Notification stored in DB");
            }
        } catch (IOException e) {
            logger.info("[notifyAdmin Exception] " + e.getMessage());
        }
    }

    /**
     * Broadcasts a notification to all currently connected admins.
     * Used by {@link NotificationService#createUpdateNotification}
     * to alert all admins when an employee or driver submits a profile update request.
     * <p>Admins who are not connected will not receive the push notification but
     * can still retrieve it via {@code GET /admin/notifications/pending}.
     * @param message
     *  The notification message to broadcast
     */
    public void broadcastToAdmins(String message) {
        sessionUserNameMap.forEach((session, userName) -> {
            try {
                session.getBasicRemote().sendText(message);
                logger.info("[broadcastToAdmins] Sent to " + userName + ": " + message);
            } catch (IOException e) {
                logger.info("[broadcastToAdmins]: " + e.getMessage());
            }
        });
    }
}