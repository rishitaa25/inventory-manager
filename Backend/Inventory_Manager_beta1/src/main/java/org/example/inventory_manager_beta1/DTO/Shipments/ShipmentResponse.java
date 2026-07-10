package org.example.inventory_manager_beta1.DTO.Shipments;

import lombok.Data;
import org.example.inventory_manager_beta1.Entities.Item;
import org.example.inventory_manager_beta1.DTO.Objects.ShortItem;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A lightweight Data Transfer Object used for sending a status message
 * from backend to frontend with information about a Shipment
 */
@Data
public class ShipmentResponse {
    /**
     * An Integer variable for transferring the Shipment's ID
     */
    private Integer shipmentId;
    /**
     * An Integer variable for transferring the employeeId of the
     * Admin who created the Shipment
     */
    private Integer creationAdminId;
    /**
     * An ArrayList variable for transferring the list of ShortItems
     * on the Shipment
     */
    private ArrayList<ShortItem> items = new ArrayList<>();
    /**
     * A LocalDate variable for transferring the arrival date of the Shipment
     */
    private LocalDate arrivalDate;
    /**
     * A boolean variable for transferring the arrived flag for the Shipment
     */
    private boolean arrived;
    /**
     * A boolean variable for transferring the offloaded flag for the Shipment
     */
    private boolean offloaded;

    /**
     * INIT: The All Args constructor for creating a ShipmentResponse
     * @param shipmentId
     *  The ID of the Shipment
     * @param creationAdminId
     *  The employeeId of the admin who created the Shipment
     * @param longItems
     *  The List of full Items that were on the Shipment
     * @param arrivalDate
     *  The date the Shipment will arrive
     * @param arrived
     *  The status of weather the Shipment has arrived
     * @param offloaded
     *  The status of weather the Shipment has been offloaded
     */
    public ShipmentResponse(Integer shipmentId,  Integer creationAdminId, List<Item> longItems, LocalDate arrivalDate, boolean arrived, boolean offloaded) {
        this.shipmentId = shipmentId;
        this.creationAdminId = creationAdminId;
        CreateShortItems(longItems);
        this.arrivalDate = arrivalDate;
        this.arrived = arrived;
        this.offloaded = offloaded;
    }

    /**
     * A logic method that converts a List of full Items to an ArrayList of
     * ShortItems by creating a shortened version of the Item using only
     * it's SKU number, name, and shipping company
     * @param longItems
     *  The List of normal Items
     */
    private void CreateShortItems(List<Item> longItems) {
        for (Item item : longItems) {
            ShortItem shortItem = new ShortItem (item.getSkuId(), item.getItemName(), item.getDescription(), item.getQuantity(), item.getShippingCompany().getShippingCompanyName());
            items.add(shortItem);
        }
    }
}
