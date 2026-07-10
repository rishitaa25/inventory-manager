package org.example.inventory_manager_beta1.DTO.Inventories;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryChangeRequest {
    @NotNull
    private Integer skuNumber;
    @NotNull
    private Integer amountOfItem;
    @NotNull
    private Integer employeeId;
}
