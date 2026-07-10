package org.example.inventory_manager_beta1.DTO.Inventories;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * A lightweight Data Transfer Object used for sending an Inventory Change
 * request from frontend to backend with information about a change to the
 * existing Inventory. NotNull constrained variables must be present in order
 * for the change to be made
 */
@Data
public class InventoryChangeRequest {
    /**
     * A NotNull constrained Integer for transferring
     * the Item's SKU number
     */
    @NotNull
    private Integer skuNumber;
    /**
     * A NotNull constrained Integer for transferring
     * the amount of change to an existing Item
     */
    @NotNull
    private Integer amountOfItem;
    /**
     * A NotNull constrained Integer for transferring
     * the ID of the Employee who made the change
     */
    @NotNull
    private Integer employeeId;
}
