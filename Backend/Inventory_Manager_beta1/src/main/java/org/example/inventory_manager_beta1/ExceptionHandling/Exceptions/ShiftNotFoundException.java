package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * A custom RuntimeException handler class designed to handle a RuntimeException thrown while
 * searching for a Shift in the database
 * @see
 *  org.example.inventory_manager_beta1.Services.ShiftService
 * @see
 *  org.example.inventory_manager_beta1.ExceptionHandling.GlobalExceptionHandler
 */
public class ShiftNotFoundException extends RuntimeException {
    /**
     * INIT: The All Args constructor for creating a ShiftNotFoundException
     * @param message
     *  The message being returned in the RuntimeException
     */
    public ShiftNotFoundException(String message) {super(message);}
}
