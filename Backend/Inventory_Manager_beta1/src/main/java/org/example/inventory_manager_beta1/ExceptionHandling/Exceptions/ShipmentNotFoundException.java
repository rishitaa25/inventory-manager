package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * A custom RuntimeException handler class designed to handle a RuntimeException thrown while
 * searching for a Shipment in the database
 * @see
 *  org.example.inventory_manager_beta1.Services.ShipmentService
 * @see
 *  org.example.inventory_manager_beta1.ExceptionHandling.GlobalExceptionHandler
 */
public class ShipmentNotFoundException extends RuntimeException {
    /**
     * INIT: The All Args constructor for creating a ShipmentNotFoundException
     * @param message
     *  The message being returned in the RuntimeException
     */
    public ShipmentNotFoundException(String message) {super(message);}
}
