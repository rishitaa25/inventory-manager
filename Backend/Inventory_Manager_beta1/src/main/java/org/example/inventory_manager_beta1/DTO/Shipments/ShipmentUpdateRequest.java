package org.example.inventory_manager_beta1.DTO.Shipments;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

/**
 * A lightweight Data Transfer Object used for sending an update request
 * from frontend to backend with updated Shipment information for an existing
 * Shipment. NotNull constrained variables must be present in order for the
 * update to be completed.
 */
@Data
public class ShipmentUpdateRequest {
    /**
     * A NotNull constrained Integer variable for transferring
     * the Shipment's identification number
     */
    @NotNull
    private Integer shipmentId;
    /**
     * A boolean variable for transferring the arrival status
     * of the Shipment
     */
    private boolean arrived;
    /**
     * A LocalDate variable for transferring the arrival
     * date of the Shipment
     */
    private LocalDate arrivalDate;
    /**
     * A boolean variable for transferring the offloaded
     * status of the Shipment
     */
    private boolean offloaded;
}
