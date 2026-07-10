package org.example.inventory_manager_beta1.DTO.Shipments;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.inventory_manager_beta1.DTO.Items.Add.AddItemRequest;
import java.time.LocalDate;
import java.util.List;

/**
 * A lightweight Data Transfer Object used for sending a shipment request
 * from frontend to backend with information about which items are being
 * requested, which drivers are assigned, and the requested arrival date.
 * NotNull and NotEmpty constrained variables must be present in order
 * for the shipment request to be made
 */
@Data
public class ShipmentRequest {
    /**
     * A NotNull constrained Integer variable for transferring
     * the ID of the Admin that made the shipment request
     */
    @NotNull
    private Integer id;
    /**
     * A NotEmpty constrained List variable of AddItemRequests
     * for transferring a list of Items being requested
     */
    @NotEmpty
    private List<AddItemRequest> items;
    /**
     * A NotNull constrained LocalDate variable for
     * transferring the shipment's requested delivery date
     */
    @NotNull
    private LocalDate requestedDeliveryDate;
    /**
     * A List variable of Integers for transferring a list
     * of Driver ID's that are assigned to the shipment
     */
    private List<Integer> driverIds;
}
