package org.example.inventory_manager_beta1.DTO.Shipments;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
//Class to handle transferring data from JSON file
public class ShippingCompanyRequest {
    @NotBlank //Only required field(s) to make a shipping company
    private String shippingCompanyName;
    @Email
    private String shippingCompanyEmail;
    private String shippingCompanyPhone;
}
