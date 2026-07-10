package org.example.inventory_manager_beta1.DTO.Inventories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * A lightweight Data Transfer Object used for sending an Inventory request
 * from frontend to backend with information about a new item being added
 * to the existing Inventory. NotBlank and NotNull constrained variables
 * must be present in order for the request to be made
 */
@Data
public class AddInventoryRequest {
    /**
     * A NotBlank constrained variable for transferring
     * the Item's name
     */
    @NotBlank
    private String itemName;
    /**
     * A String variable for transferring the Item's description
     */
    private String itemDescription;
    /**
     * A NotNull constrained variable for transferring
     * the Item's amount
     */
    @NotNull
    private Integer amountOfItem;
}