package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * A custom RuntimeException handler class designed to handle a RuntimeException thrown while
 * searching for an Admin in the database
 * @see
 *  org.example.inventory_manager_beta1.Services.AdminService
 * @see
 *  org.example.inventory_manager_beta1.ExceptionHandling.GlobalExceptionHandler
 */
public class AdminNotFoundException extends RuntimeException {
    /**
     * INIT: The All Args constructor for creating an AdminNotFoundException
     * @param message
     *  The message being returned in the RuntimeException
     */
    public AdminNotFoundException(String message) {super(message);}
}
