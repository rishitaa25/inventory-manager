package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

import lombok.Getter;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;

/**
 * Runtime exception thrown when an authenticated user attempts to access
 * a resource or method that requires a higher {@link AccessLevel} than they hold.
 * <p>This exception is thrown by {@code AccessControl} (the AOP aspect) when
 * a method annotated with {@code @RequiresAccess} is called by a user whose
 * access level ordinal is less than the required level's ordinal.
 * <p>It is intended to be caught by a global exception handler and returned
 * to the client as a {@code 403 FORBIDDEN} response.
 * <p>Usage example in {@code AccessControl}:
 * <pre>
 *   throw new InsufficientAccessException(AccessLevel.ADMIN, AccessLevel.EMPLOYEE);
 *   // Message: "Access denied. Required level: ADMIN, your level: EMPLOYEE"
 * </pre>
 */
@Getter
public class InsufficientAccessLevelException extends RuntimeException {

    /**
     * The access level that was required to perform the action.
     */
    private final AccessLevel requiredLevel;

    /**
     * The access level the caller actually holds.
     */
    private final AccessLevel callerLevel;

    /**
     * INIT: Constructs the exception with a descriptive message built from
     * the required and caller access levels.
     * @param requiredLevel
     *  The minimum {@link AccessLevel} needed for the action
     * @param callerLevel
     *  The {@link AccessLevel} the caller actually has
     */
    public InsufficientAccessLevelException(AccessLevel requiredLevel, AccessLevel callerLevel) {
        super("Access denied. Required level: " + requiredLevel + ", your level: " + callerLevel);
        this.requiredLevel = requiredLevel;
        this.callerLevel = callerLevel;
    }
}

