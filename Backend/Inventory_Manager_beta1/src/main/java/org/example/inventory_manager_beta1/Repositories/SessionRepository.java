package org.example.inventory_manager_beta1.Repositories;

import org.example.inventory_manager_beta1.Entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * JPA repository for {@link Session} entities.
 * <p>Provides standard CRUD operations inherited from {@link JpaRepository},
 * plus custom lookup and cleanup methods used by {@link org.example.inventory_manager_beta1.Services.SessionService}.
 */
public interface SessionRepository extends JpaRepository<Session, String> {
    /**
     * SEARCH: Finds an active session by its UUID string.
     * Called on every authenticated request by {@code JwtAuthFilter} to verify
     * the token is still valid (i.e. the user hasn't logged out).
     * @param sessionId
     *  The UUID string extracted from the incoming JWT
     * @return
     *  The {@link Session} if it exists (user is logged in),
     *  or an empty {@link Optional} if not (user has logged out)
     */
    Optional<Session> findBySessionId(String sessionId);

    /**
     * DELETE: Removes all sessions belonging to a specific employee.
     * Useful for force-logging out a user from all devices at once,
     * for example after a password change or account suspension.
     * @param employeeId
     *  The ID of the employee whose sessions should all be invalidated
     */
    void deleteAllByEmployeeId(Integer employeeId);
}
