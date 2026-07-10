package org.example.inventory_manager_beta1.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing an active user session in the database.
 * <p>A {@code Session} row is created whenever any {@code Employee}, {@code Driver},
 * or {@code Admin} logs in successfully, and deleted when they log out. If a session
 * row doesn't exist, the corresponding JWT is immediately considered invalid.
 * {@code JwtAuthFilter} will find no session and skip authentication entirely.
 * <p>The JWT sent to the client carries <em>only</em> the {@code sessionId}.
 * All real auth data (who this user is, what they can access) lives here.
 * This design allows instant logout where deleting the row invalidates the token
 * regardless of its expiry time.
 */
@Entity
@Getter
@Setter
@Table(name = "sessions")
@NoArgsConstructor
public class Session {

    /**
     * The unique session identifier — a UUID generated at login time.
     * This is what gets embedded in the JWT as the subject claim and
     * looked up on every authenticated request.
     */
    @Id
    @Column(name = "session_id", updatable = false, nullable = false)
    private String sessionId;

    /**
     * The ID of the employee who owns this session.
     * Works for {@code Employee}, {@code Driver}, and {@code Admin} since
     * they all share the same {@code employee_id} column via inheritance.
     */
    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    /**
     * The access level of the session owner, copied from their entity at login time.
     * Stored here so every authenticated request only needs one DB lookup
     * (the session row) rather than also fetching the employee record.
     */
    @Column(name = "access_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;

    /**
     * The username of the session owner.
     * Used to populate the Spring Security principal so controllers can
     * identify who is making the request.
     */
    @Column(name = "user_name", nullable = false)
    private String userName;

    /**
     * Timestamp of when this session was created (i.e. when the user logged in).
     * Useful for auditing and for identifying stale sessions.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * INIT: Creates a new session with an automatically generated UUID session ID.
     * @param employeeId
     *  The ID of the employee logging in
     * @param accessLevel
     *  Their access level at the time of login
     * @param userName
     *  Their username
     */
    public Session(Integer employeeId, AccessLevel accessLevel, String userName) {
        this.sessionId = UUID.randomUUID().toString();
        this.employeeId = employeeId;
        this.accessLevel = accessLevel;
        this.userName = userName;
        this.createdAt = LocalDateTime.now();
    }
}
