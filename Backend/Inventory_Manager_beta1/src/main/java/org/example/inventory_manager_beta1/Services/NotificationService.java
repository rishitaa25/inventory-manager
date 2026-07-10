package org.example.inventory_manager_beta1.Services;

import org.example.inventory_manager_beta1.DTO.Employees.Update.UpdateEmployeeRequest;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.DataEnums.Status;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.NotificationNotFoundException;
import org.example.inventory_manager_beta1.Entities.Notification;
import org.example.inventory_manager_beta1.MainApplicationClasses.WebSockets.WS_AdminNotification;
import org.example.inventory_manager_beta1.MainApplicationClasses.WebSockets.WS_EmployeeNotification;
import org.example.inventory_manager_beta1.Repositories.NotificationRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing notifications and the employee update request approval workflow.
 *
 * <p>Notifications serve two purposes:
 * <ol>
 *   <li><b>General notifications:</b> Informational messages sent to a specific
 *       access level via {@link #createGeneralNotification(AccessLevel, String, LocalDate)}</li>
 *   <li><b>Update request notifications:</b> Created when an employee or driver
 *       requests a profile change via {@link #createUpdateNotification(UpdateEmployeeRequest, String)}.
 *       Stored as {@code PENDING} until an admin approves or rejects them.</li>
 * </ol>
 *
 * <p>WebSocket alerts are pushed at two points:
 * <ul>
 *   <li>When a request is created, all connected admins are alerted</li>
 *   <li>When a request is approved or rejected, the requesting employee is alerted</li>
 * </ul>
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmployeeService employeeService;
    private final DriverService driverService;
    private final WS_AdminNotification wsAdminNotification;
    private final WS_EmployeeNotification wsEmployeeNotification;

    /**
     * INIT: Constructs the service with its required dependencies.
     * @param notificationRepository
     *  The JPA repository for notification persistence
     * @param employeeService
     *  Used to apply approved update requests and look up usernames
     * @param wsAdminNotification
     *  WebSocket endpoint for pushing alerts to connected admins
     * @param wsEmployeeNotification
     *  WebSocket endpoint for pushing results to requesting employees
     */
    public NotificationService(NotificationRepository notificationRepository, EmployeeService employeeService, DriverService driverService, WS_AdminNotification wsAdminNotification, WS_EmployeeNotification wsEmployeeNotification) {
        this.notificationRepository = notificationRepository;
        this.employeeService = employeeService;
        this.driverService = driverService;
        this.wsAdminNotification = wsAdminNotification;
        this.wsEmployeeNotification = wsEmployeeNotification;
    }

    /*==================== SEARCH METHODS ====================*/

    /**
     * SEARCH: Finds a notification by its ID.
     * @param id
     *  The ID of the notification
     * @return
     *  The matching {@link Notification}
     * @throws NotificationNotFoundException
     *  If no notification with that ID exists
     */
    public Notification findById(Integer id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification with ID: " + id + " not found."));
    }

    /**
     * SEARCH: Finds all notifications intended for a specific access level.
     * @param recipient
     *  The {@link AccessLevel} of the intended recipient
     * @return
     *  A list of all notifications for that recipient
     */
    public List<Notification> findByRecipient(AccessLevel recipient) {return notificationRepository.findByRecipient(recipient);}

    /**
     * SEARCH: Finds all notifications with a specific status.
     * @param status
     *  The {@link Status} to filter by
     * @return
     *  A list of all matching notifications
     */
    public List<Notification> findByStatus(Status status) {return notificationRepository.findByStatus(status);}

    /**
     * SEARCH: Finds all notifications for a specific recipient and status.
     * The primary query for the admin approval workflow —
     * e.g. all PENDING notifications sent to ADMIN.
     * @param recipient
     *  The {@link AccessLevel} of the intended recipient
     * @param status
     *  The {@link Status} to filter by
     * @return
     *  A list of all matching notifications
     */
    public List<Notification> findByRecipientAndStatus(AccessLevel recipient, Status status) {return notificationRepository.findByRecipientAndStatus(recipient, status);}

    /**
     * SEARCH: Finds all notifications created on a specific date.
     * @param date
     *  The date to search for
     * @return
     *  A list of all notifications from that date
     */
    public List<Notification> findByDate(LocalDate date) {return notificationRepository.findByDate(date);}

    /*==================== LOGIC METHODS ====================*/

    /**
     * UPDATE: Saves a notification to the database.
     * @param notification
     *  The {@link Notification} to save
     */
    public void saveNotification(Notification notification) {notificationRepository.save(notification);}

    /**
     * CREATE: Creates and saves a general notification to a specific access level.
     * Used by {@link ShipmentService} when shipment events occur.
     * @param recipient
     *  The {@link AccessLevel} of the intended recipient
     * @param message
     *  The notification message
     * @param date
     *  The date of the notification
     */
    public void createGeneralNotification(AccessLevel recipient, String message, LocalDate date) {
        Notification notification = new Notification(recipient, message, date);
        saveNotification(notification);
    }

    /**
     * CREATE: Creates a pending update request notification and pushes a real-time
     * WebSocket alert to all currently connected admins.
     * <p>Serializes the {@link UpdateEmployeeRequest} as JSON into the notification
     * message so admins can see exactly what the employee or driver wants to change.
     * Admins who are offline will still see the request via
     * {@code GET /admin/notifications/pending} when they next log in.
     * @param updateRequest
     *  The DTO containing the employee's or driver's requested changes
     * @throws RuntimeException
     *  If the request cannot be serialized to JSON
     */
    public void createUpdateNotification(UpdateEmployeeRequest updateRequest, String requestorType) {
        Notification notification = new Notification(
                AccessLevel.ADMIN,
                LocalDate.now(),
                Status.PENDING,
                updateRequest.getEmployeeId(),
                updateRequest.getFirstName(),
                updateRequest.getLastName(),
                updateRequest.getUserName(),
                updateRequest.getEmail(),
                updateRequest.getPhoneNumber(),
                requestorType
        );
        saveNotification(notification);
        wsAdminNotification.broadcastToAdmins("New update request from Employee ID: " + updateRequest.getEmployeeId());
    }

    /**
     * UPDATE: Approves a pending update request notification.
     * <p>Applies the stored field changes to the employee's or driver's record,
     * marks the notification as {@code APPROVED}, and pushes a WebSocket
     * notification to the requesting employee using their pre-update username.
     * @param notificationId
     *  The ID of the PENDING notification to approve
     * @throws NotificationNotFoundException
     *  If no notification with that ID exists
     */
    public void approveUpdateRequest(Integer notificationId) {
        Notification notification = findById(notificationId);
        Integer requesterId = notification.getRequestedByEmployeeId();

        UpdateEmployeeRequest updateRequest = new UpdateEmployeeRequest();
        updateRequest.setEmployeeId(requesterId);
        updateRequest.setFirstName(notification.getFirstName());
        updateRequest.setLastName(notification.getLastName());
        updateRequest.setUserName(notification.getUserName());
        updateRequest.setEmail(notification.getEmail());
        updateRequest.setPhoneNumber(notification.getPhoneNumber());

        String userName;
        if ("DRIVER".equals(notification.getRequesterType())) {
            userName = driverService.findById(requesterId).getUserName();
            driverService.updateDriver(updateRequest);
        } else {
            userName = employeeService.findById(requesterId).getUserName();
            employeeService.updateEmployee(updateRequest);
        }
        notification.setStatus(Status.APPROVED);
        saveNotification(notification);
        wsEmployeeNotification.notifyEmployee(userName, "Your update request has been approved and your profile has been updated.");
    }

    /**
     * UPDATE: Rejects a pending update request notification.
     * <p>Marks the notification as {@code REJECTED} without applying any changes,
     * and pushes a WebSocket notification to the requesting employee.
     * @param notificationId
     *  The ID of the PENDING notification to reject
     * @throws NotificationNotFoundException
     *  If no notification with that ID exists
     */
    public void rejectUpdateRequest(Integer notificationId) {
        Notification notification = findById(notificationId);
        notification.setStatus(Status.REJECTED);
        saveNotification(notification);

        Integer requesterId = notification.getRequestedByEmployeeId();
        String userName;
        if ("DRIVER".equals(notification.getRequesterType())) {
            userName = driverService.findById(requesterId).getUserName();
        } else {
            userName = employeeService.findById(requesterId).getUserName();
        }
        wsEmployeeNotification.notifyEmployee(userName, "Your update request has been rejected. Please contact your manager for details.");
    }

    /**
     * DELETE: Deletes a specific notification by its ID.
     * @param id
     *  The ID of the notification to delete
     */
    public void deleteNotification(Integer id) {
        notificationRepository.deleteById(id);
    }

    /**
     * DELETE: Deletes all notifications from the database.
     */
    public void deleteAll() {
        notificationRepository.deleteAll();
    }
}