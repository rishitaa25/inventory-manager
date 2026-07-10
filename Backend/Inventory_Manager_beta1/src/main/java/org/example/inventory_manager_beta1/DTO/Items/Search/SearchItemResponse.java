package org.example.inventory_manager_beta1.DTO.Items.Search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.inventory_manager_beta1.DataEnums.StorageType;
import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchItemResponse {
    private Integer itemSku;
    private String itemName;
    private String storageLocation;
    private Integer quantity;
    private Double weight;
    private StorageType storageType;
    private LocalDate arrivalDate;
    private Optional<Integer> shipmentId;
    private Optional<String> shippingCompany;
}
