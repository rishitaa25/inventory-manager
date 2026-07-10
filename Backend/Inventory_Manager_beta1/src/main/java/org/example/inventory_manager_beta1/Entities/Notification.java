package org.example.inventory_manager_beta1.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.DataEnums.Status;
import java.time.LocalDate;

/**
 * Entity representing a notification in the system.
 *
 * <p>Notifications serve two purposes:
 * <ol>
 *   <li><b>General notifications</b> — informational messages sent to a specific
 *       access level. Only {@code message}, {@code recipient}, {@code date}, and
 *       {@code status} are populated. All update request fields are null.</li>
 *   <li><b>Update request notifications</b> — sent to {@code ADMIN} when an employee
 *       or driver requests a change to their profile. The requested changes are stored
 *       as individual nullable columns so the response serializes cleanly without any
 *       JSON string escaping. {@code message} is null for these.</li>
 * </ol>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The access level of the intended recipient.
     * Used to filter which notifications each role can see.
     */
    @Column(name = "recipient")
    @Enumerated(EnumType.STRING)
    private AccessLevel recipient;

    /**
     * Plain text message body for general notifications.
     * Null for update request notifications — use the individual fields instead.
     */
    @Column(name = "message")
    private String message;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * The ID of the employee who submitted the update request.
     * Only populated for update request notifications.
     */
    @Column(name = "requested_by_employee_id")
    private Integer requestedByEmployeeId;

    /**
     * The type of user who submitted the update request.
     * "DRIVER" or "EMPLOYEE". Only populated for update request notifications.
     */
    @Column(name = "requester_type")
    private String requesterType;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "driver")
    private String driver;

    /**
     * INIT: Constructor for general notifications.
     * @param recipient
     *  The access level of the intended recipient
     * @param message
     *  The plain text notification message
     * @param date
     *  The date the notification was created
     */
    public Notification(AccessLevel recipient, String message, LocalDate date) {
        this.recipient = recipient;
        this.message = message;
        this.date = date;
    }

    /**
     * INIT: Constructor for general notifications with an explicit status.
     * @param recipient
     *  The access level of the intended recipient
     * @param message
     *  The plain text notification message
     * @param date
     *  The date the notification was created
     * @param status
     *  The initial status of the notification
     */
    public Notification(AccessLevel recipient, String message, LocalDate date, Status status) {
        this.recipient = recipient;
        this.message = message;
        this.date = date;
        this.status = status;
    }

    /**
     * INIT: Constructor for update request notifications.
     * Stores each requested change as an individual column so the response
     * serializes cleanly as a proper JSON object.
     * @param recipient
     *  The access level of the intended recipient (ADMIN)
     * @param date
     *  The date the request was submitted
     * @param status
     *  {@code PENDING} for new update requests
     * @param requestedByEmployeeId
     *  The ID of the employee submitting the request
     * @param firstName
     *  The requested new first name, or null if not changing
     * @param lastName
     *  The requested new last name, or null if not changing
     * @param userName
     *  The requested new username, or null if not changing
     * @param email
     *  The requested new email, or null if not changing
     * @param phoneNumber
     *  The requested new phone number, or null if not changing
     */
    public Notification(AccessLevel recipient, LocalDate date, Status status, Integer requestedByEmployeeId, String firstName, String lastName, String userName, String email, String phoneNumber, String requesterType) {
        this.recipient = recipient;
        this.date = date;
        this.status = status;
        this.requestedByEmployeeId = requestedByEmployeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.requesterType = requesterType;
    }
}