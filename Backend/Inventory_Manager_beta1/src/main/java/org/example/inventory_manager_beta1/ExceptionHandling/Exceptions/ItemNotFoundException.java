package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * A custom RuntimeException handler class designed to handle a RuntimeException thrown while
 * searching for an Item in the database
 * @see
 *  org.example.inventory_manager_beta1.Services.ItemService
 * @see
 *  org.example.inventory_manager_beta1.ExceptionHandling.GlobalExceptionHandler
 */
public class ItemNotFoundException extends RuntimeException {
    /**
     * INIT: The All Args constructor for creating an ItemNotFoundException
     * @param message
     *  The message being returned in the RuntimeException
     */
    public ItemNotFoundException(String message) {super(message);}
}
