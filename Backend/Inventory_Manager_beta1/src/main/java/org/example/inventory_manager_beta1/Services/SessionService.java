package org.example.inventory_manager_beta1.Services;

import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.Entities.Session;
import org.example.inventory_manager_beta1.Repositories.SessionRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Service for managing user sessions throughout the application.
 * <p>Sessions are the server-side component of the authentication system.
 * When a user logs in, a {@link Session} row is created in the database and
 * its UUID is embedded in a JWT returned to the client. On every subsequent
 * request, {@code JwtAuthFilter} calls {@link #findSession(String)} to verify
 * the session is still active. When the user logs out, {@link #closeSession(String)}
 * deletes the row which immediately invalidates their token.
 */
@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    /**
     * INIT: Constructs the service with its required repository dependency.
     * @param sessionRepository
     *  The JPA repository for session persistence
     */
    public SessionService(SessionRepository sessionRepository) {this.sessionRepository = sessionRepository;}

    /**
     * SEARCH: Looks up an active session by its UUID.
     * Called by {@code JwtAuthFilter} on every incoming authenticated request.
     * @param id
     *  The UUID string extracted from the incoming JWT
     * @return
     *  The {@link Session} if active, or an empty {@link Optional} if the
     *  user has logged out or the session never existed
     */
    public Optional<Session> findSession(String id) {
        return sessionRepository.findBySessionId(id);
    }

    /**
     * CREATE: Opens a new session for a user who has just successfully logged in.
     * The session UUID is generated automatically inside the {@link Session} constructor.
     * @param employeeId
     *  The ID of the employee logging in
     * @param accessLevel
     *  Their access level at the time of login
     * @param userName
     *  Their username
     * @return
     *  The newly created and saved {@link Session}
     */
    public Session createSession(Integer employeeId, AccessLevel accessLevel, String userName) {
        Session session = new Session(employeeId, accessLevel, userName);
        return sessionRepository.save(session);
    }

    /**
     * DELETE: Closes a specific session by its UUID.
     * Used for logout and after this call, any JWT containing this session ID
     * will be rejected by {@code JwtAuthFilter}.
     * @param sessionID
     *  The UUID of the session to close
     */
    public void closeSession(String sessionID) {sessionRepository.deleteById(sessionID);}

    /**
     * DELETE: Closes all sessions belonging to a specific employee.
     * Use this to force a full logout from all devices, for example after
     * a password change or account suspension.
     * @param employeeId
     *  The ID of the employee whose sessions should all be invalidated
     */
    public void closeAllSessions(Integer employeeId) {sessionRepository.deleteAllByEmployeeId(employeeId);}
}
