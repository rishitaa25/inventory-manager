package org.example.inventory_manager_beta1.DTO.Items;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.inventory_manager_beta1.DataEnums.StorageType;
import java.time.LocalDate;

@Data
//Class to handle transferring data from JSON file
public class AddItemRequest {
    @NotBlank //Only required field(s) for making an item
    private String itemName;
    private Integer shipmentCompanyId;
    private String storageLocation;
    @NotNull
    private Integer quantity;
    private Double weight;
    private StorageType storageType;
    private LocalDate arrivalDate;
}
