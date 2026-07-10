package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(String message) {
        super(message);
    }
}
