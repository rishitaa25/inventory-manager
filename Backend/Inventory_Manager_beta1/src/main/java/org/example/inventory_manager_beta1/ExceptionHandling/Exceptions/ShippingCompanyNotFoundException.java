package org.example.inventory_manager_beta1.ExceptionHandling.Exceptions;

/**
 * A custom RuntimeException handler class designed to handle a RuntimeException thrown while
 * searching for an ShippingCompany in the database
 * @see
 *  org.example.inventory_manager_beta1.Services.ShippingCompanyService
 * @see
 *  org.example.inventory_manager_beta1.ExceptionHandling.GlobalExceptionHandler
 */
public class ShippingCompanyNotFoundException extends RuntimeException {
    /**
     * INIT: The All Args constructor for creating an ShippingCompanyNotFoundException
     * @param message
     *  The message being returned in the RuntimeException
     */
    public ShippingCompanyNotFoundException(String message) {super(message);}
}
