package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * A custom RuntimeException handler class designed to handle a RuntimeException thrown while
 * searching for an InventoryChange in the database
 * @see
 *  org.example.inventory_manager_beta1.Services.InventoryChangeService
 * @see
 *  org.example.inventory_manager_beta1.ExceptionHandling.GlobalExceptionHandler
 */
public class InventoryChangeNotFoundException extends RuntimeException {
    /**
     * INIT: The All Args constructor for creating an InventoryChangeNotFoundException
     * @param message
     *  The message being returned in the RuntimeException
     */
    public InventoryChangeNotFoundException(String message) {super(message);}
}
