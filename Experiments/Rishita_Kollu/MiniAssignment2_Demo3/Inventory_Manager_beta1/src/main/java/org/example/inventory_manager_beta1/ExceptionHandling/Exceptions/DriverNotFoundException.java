package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * A custom Exception handler class for more accurate HTTP error handling
 * regarding Drivers
 */
public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException(String message) {super(message);}
}
