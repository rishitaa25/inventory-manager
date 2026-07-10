package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * A custom RuntimeException handler class designed to handle a RuntimeException thrown while
 * searching for an Employee in the database
 * @see
 *  org.example.inventory_manager_beta1.Services.EmployeeService
 * @see
 *  org.example.inventory_manager_beta1.ExceptionHandling.GlobalExceptionHandler
 */
public class EmployeeNotFoundException extends RuntimeException {
    /**
     * INIT: The All Args constructor for creating an EmployeeNotFoundException
     * @param message
     *  The message being returned in the RuntimeException
     */
    public EmployeeNotFoundException(String message) {super(message);}
}
