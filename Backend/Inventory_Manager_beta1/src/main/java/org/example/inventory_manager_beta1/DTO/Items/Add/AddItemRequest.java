package org.example.inventory_manager_beta1.DTO.Items.Add;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.inventory_manager_beta1.DataEnums.StorageType;
import java.time.LocalDate;

/**
 * A lightweight Data Transfer Object used for sending a new Item request
 * from frontend to backend with information about the new Item being created.
 * NotBlank and NotNull constrained variable must be present in order for
 * the Item to be created
 */
@Data
public class AddItemRequest {
    /**
     * A NotBlank constrained String variable for transferring
     * the Item's name
     */
    @NotBlank
    private String itemName;
    /**
     * A String variable for transferring the Item's description
     */
    private String description;
    /**
     * A String variable for transferring the Item's storage location
     */
    @NotBlank
    private String storageLocation;
    /**
     * A NotNull constrained Integer variable for transferring
     * the Item amount
     */
    @NotNull
    private Integer quantity;
    /**
     * A Double variable for transferring the Item's weight
     */
    private Double weight;
    /**
     * A StorageType Enumerated Constant for transferring
     * the Item's storage type
     */
    @NotNull
    private StorageType storageType;
    /**
     * A LocalDate variable for transferring the date
     * the Item arrived
     */
    private LocalDate arrivalDate;
}
