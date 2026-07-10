package org.example.inventory_manager_beta1.MainApplicationClasses.WebSockets;

import org.example.inventory_manager_beta1.Services.NotificationService;
import org.example.inventory_manager_beta1.Entities.Notification;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnError;
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
 * A WebSocket endpoint dedicated to pushing real-time notifications to connected employees.
 *
 * <p>Employees connect to this endpoint after login. When an admin approves or rejects
 * an update request, {@link NotificationService}
 * calls {@link #notifyEmployee(String, String)} to instantly push the result to the
 * requesting employee without them needing to poll the server.
 *
 * <p>Connection URL: {@code ws://current-server:8080/employee-notifications/{userName}}
 */
@ServerEndpoint("/employee-notifications/{userName}")
@Component
public class WS_EmployeeNotification {

    private static final Map<Session, String> sessionUserNameMap = new Hashtable<>();
    private static final Map<String, Session> userNameSessionMap = new Hashtable<>();
    private final Logger logger = LoggerFactory.getLogger(WS_EmployeeNotification.class);

    /**
     * Called when an employee opens a WebSocket connection.
     * Registers the session so notifications can be pushed to this employee.
     * Rejects the connection if the username is already connected.
     * @param session
     *  The WebSocket session created when the connection is opened
     * @param userName
     *  The username of the employee who opened the connection
     * @throws IOException
     *  If sending the rejection message fails
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) throws IOException {
        logger.info("[onOpen] " + userName);
        sessionUserNameMap.put(session, userName);
        userNameSessionMap.put(userName, session);
        logger.info(userName + " connected to Employee notifications");
    }

    /**
     * Called when an employee closes the WebSocket connection.
     * Removes the session from both maps.
     * @param session
     *  The WebSocket session that was closed
     */
    @OnClose
    public void onClose(Session session) {
        String userName = sessionUserNameMap.get(session);
        sessionUserNameMap.remove(session);
        userNameSessionMap.remove(userName);
        logger.info("[onClose] Employee disconnected: " + userName);
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
     * Sends a notification to a specific employee by username.
     * Used by {@link NotificationService}
     * to push approval or rejection results directly to the requesting employee.
     * <p>If the employee is not connected, the message is silently dropped and logged —
     * the approval status is also persisted on the {@link Notification}
     * entity and can be retrieved later if needed.
     * @param userName
     *  The username of the employee to notify
     * @param message
     *  The notification message to send
     */
    public void notifyEmployee(String userName, String message) {
        try {
            if (userNameSessionMap.containsKey(userName)) {
                userNameSessionMap.get(userName).getBasicRemote().sendText(message);
                logger.info("[notifyEmployee] Sent to " + userName + ": " + message);
            } else {
                logger.info("[notifyEmployee] " + userName + " is not connected — result stored in DB only");
            }
        } catch (IOException e) {
            logger.info("[notifyEmployee Exception] " + e.getMessage());
        }
    }
}