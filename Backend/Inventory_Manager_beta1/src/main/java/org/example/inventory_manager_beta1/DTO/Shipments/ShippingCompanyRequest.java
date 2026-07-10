package org.example.inventory_manager_beta1.DTO.Shipments;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * A lightweight Data Transfer Object used for sending a ShippingCompany request
 * from frontend to backend with information about a new shipping company being
 * added to the database. NotBlank and NotNull constrained variables must be
 * present in order for the shipping company to be made
 */
@Data
public class ShippingCompanyRequest {
    /**
     * A NotBlank constrained String variable for transferring
     * the shipping company's name
     */
    @NotBlank
    private String shippingCompanyName;
    /**
     * An Email constrained String variable for transferring
     * the shipping company's email address
     */
    @Email
    private String shippingCompanyEmail;
    /**
     * A String variable for transferring the shipping companies
     * phone number
     */
    private String shippingCompanyPhone;
}
