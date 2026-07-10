package org.example.inventory_manager_beta1.DTO.Inventories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddInventoryRequest {
    @NotBlank
    private String itemName;
    private String itemDescription;
    @NotNull
    private Integer amountOfItem;
}