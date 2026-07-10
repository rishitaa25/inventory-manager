package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * A custom Exception handler class for more accurate HTTP error handling
 * regarding Employees
 */
public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String message) {super(message);}
}
