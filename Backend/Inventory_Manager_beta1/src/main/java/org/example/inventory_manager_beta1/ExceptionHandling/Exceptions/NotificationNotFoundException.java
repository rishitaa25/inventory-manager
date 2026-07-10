package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String message) {
        super(message);
    }
}
