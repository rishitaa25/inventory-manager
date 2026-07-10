package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * A custom RuntimeException handler class designed to handle a RuntimeException thrown while
 * searching for a Driver in the database
 * @see
 *  org.example.inventory_manager_beta1.Services.DriverService
 * @see
 *  org.example.inventory_manager_beta1.ExceptionHandling.GlobalExceptionHandler
 */
public class DriverNotFoundException extends RuntimeException {
    /**
     * INIT: The All Args constructor for creating a DriverNotFoundException
     * @param message
     *  The message being returned in the RuntimeException
     */
    public DriverNotFoundException(String message) {super(message);}
}
