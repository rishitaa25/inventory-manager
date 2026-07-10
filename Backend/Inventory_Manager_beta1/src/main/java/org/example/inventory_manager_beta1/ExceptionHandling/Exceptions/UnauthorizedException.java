package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * Runtime exception thrown when a request arrives without a valid authenticated
 * session — either no token was provided, the token has expired, or the session
 * row no longer exists in the database (user logged out).
 * <p>Thrown by {@code AccessControl} (the AOP aspect) before any access level
 * check is performed, since there is no identity to check against.
 * <p>Intended to be caught by {@code GlobalExceptionHandler} and returned to
 * the client as a {@code 401 UNAUTHORIZED} response.
 * <p>Usage example in {@code AccessControl}:
 * <pre>
 *   throw new UnauthorizedException("No valid session. Please log in again.");
 * </pre>
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * INIT: Constructs the exception with a descriptive message.
     *
     * @param message A message describing why the request is unauthorized
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}