package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * A custom RuntimeException handler class designed to handle a RuntimeException thrown while
 * searching for an Inventory in the database
 * @see
 *  org.example.inventory_manager_beta1.Services.InventoryService
 * @see
 *  org.example.inventory_manager_beta1.ExceptionHandling.GlobalExceptionHandler
 */
public class InventoryNotFoundException extends RuntimeException {
    /**
     * INIT: The All Args constructor for creating an InventoryNotFoundException
     * @param message
     *  The message being returned in the RuntimeException
     */
    public InventoryNotFoundException(String message) {
        super(message);
    }
}
