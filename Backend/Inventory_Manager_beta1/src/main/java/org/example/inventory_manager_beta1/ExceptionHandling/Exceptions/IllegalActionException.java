package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * A custom RuntimeException handler class designed to handle a RuntimeException thrown while
 * decreasing an Item quantity below 0
 * @see
 *  org.example.inventory_manager_beta1.Services.ItemService
 * @see
 *  org.example.inventory_manager_beta1.ExceptionHandling.GlobalExceptionHandler
 */
public class IllegalActionException extends RuntimeException {
    /**
     * INIT: The All Args constructor for creating an IllegalActionException
     * @param message
     *  The message being returned in the RuntimeException
     */
    public IllegalActionException(String message) {
        super(message);
    }
}
