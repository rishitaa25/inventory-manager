package org.example.inventory_manager_beta1.DTO.Shipments;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.inventory_manager_beta1.DTO.Items.AddItemRequest;
import java.time.LocalDate;
import java.util.List;

@Data
//Class to handle transferring data from JSON file
public class ShipmentRequest {
    @NotNull
    private Integer id;
    @NotEmpty
    private List<AddItemRequest> items;
    @NotNull
    private LocalDate requestedDeliveryDate;
    private List<Integer> driverIds;
}
