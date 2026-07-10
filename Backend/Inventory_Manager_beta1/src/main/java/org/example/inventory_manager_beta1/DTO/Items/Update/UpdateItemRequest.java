package org.example.inventory_manager_beta1.DTO.Items.Update;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.inventory_manager_beta1.DataEnums.StorageType;
import java.time.LocalDate;

/**
 * A lightweight Data Transfer Object used for sending an update request
 * from frontend to backend with updated Item information for an existing Item.
 * NotNull constrained variables must be present in order for the update
 * to be completed.
 */
@Data
public class UpdateItemRequest {
    /**
     * A NotNull constrained Integer variable for transferring the
     * Item's SKU number
     */
    @NotNull
    private Integer itemSKU;
    /**
     * A String variable for transferring the Item's name
     */
    private String itemName;
    /**
     * An Integer variable for transferring the Item's quantity
     */
    private Integer quantity;
    /**
     * A String variable for transferring the Item's storage location
     */
    private String storageLocation;
    /**
     * A String variable for transferring the Item's weight
     */
    private Double weight;
    /**
     * A StorageType Enumerated Constant for transferring the Item's storage type
     */
    private StorageType storageType;
    /**
     * A LocalDate variable for transferring the Item's arrival date
     */
    private LocalDate arrivalDate;
}
