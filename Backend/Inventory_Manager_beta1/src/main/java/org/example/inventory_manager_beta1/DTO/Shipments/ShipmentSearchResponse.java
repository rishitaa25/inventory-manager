package org.example.inventory_manager_beta1.DTO.Shipments;

import lombok.Data;
import org.example.inventory_manager_beta1.DTO.Objects.ShortItem;
import org.example.inventory_manager_beta1.Entities.Driver;
import org.example.inventory_manager_beta1.Entities.Item;
import org.example.inventory_manager_beta1.Entities.ShippingCompany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A lightweight Data Transfer Object used for sending a formatted JSON
 * from backend to frontend with details about a Shipment
 */
@Data
public class ShipmentSearchResponse {
    /**
     * An Integer variable for containing/transferring the Shipment's ID
     */
    private Integer shipmentId;
    /**
     * A LocalDate variable for containing/transferring the Shipment's arrival date
     */
    private LocalDate arrivalDate;
    /**
     * A boolean variable for containing/transferring the Shipment's arrived status
     */
    private boolean arrived;
    /**
     * A boolean variable for containing/transferring the Shipment's offloaded status
     */
    private boolean offloaded;
    /**
     * A List variable for containing/transferring the Shipment's Items
     */
    private List<ShortItem> items = new ArrayList<>();
    /**
     * A List variable for containing/transferring the ShippingCompanies sending Items on the Shipment
     */
    private List<String> shippingCompanies = new  ArrayList<>();
    /**
     * A List variable for containing/transferring the Shipment's Drivers
     */
    private List<String> drivers = new ArrayList<>();
    /**
     * A String variable for containing/transferring the username of the Admin who created the Shipment
     */
    private String creationAdmin;

    /**
     * INIT: The All Args constructor for creating a ShipmentSearchResponse
     * @param shipmentId
     *  The ID of the Shipment
     * @param arrivalDate
     *  The date the Shipment arrives
     * @param arrived
     *  The Shipment's arrival status
     * @param offloaded
     *  The Shipment's offloaded status
     * @param items
     *  The list of Items on the Shipment
     * @param shippingCompanies
     *  The list of ShippingCompanies sending Items
     * @param drivers
     *  The list of Drivers assigned to the Shipment
     * @param creationAdmin
     *  The username of the Admin who created the Shipment
     */
    public ShipmentSearchResponse(Integer shipmentId, LocalDate arrivalDate, boolean arrived, boolean offloaded, List<Item> items, List<ShippingCompany> shippingCompanies, List<Driver> drivers, String creationAdmin) {
        this.shipmentId = shipmentId;
        this.arrivalDate = arrivalDate;
        this.arrived = arrived;
        this.offloaded = offloaded;
        CreateShortLists(items, shippingCompanies, drivers);
        this.creationAdmin = creationAdmin;
    }

    /**
     * A logic method that formats the lists of full Items, ShippingCompanies,
     * and Drivers passed in and adds the desired data fields to the List
     * variables in the ShipmentSearchResponse DTO
     * @param longItems
     *  The List of normal Items
     * @param shippingCompanies
     *  The List of ShippingCompanies sending Items
     * @param drivers
     *  The List of Drivers assigned to the Shipment
     */
    private void CreateShortLists(List<Item> longItems, List<ShippingCompany> shippingCompanies, List<Driver> drivers) {
        for (Item item : longItems) {
            ShortItem shortItem = new ShortItem (item.getSkuId(), item.getItemName(), item.getDescription(), item.getQuantity(), item.getShippingCompany().getShippingCompanyName());
            items.add(shortItem);
        }
        for (ShippingCompany shippingCompany : shippingCompanies) {
            this.shippingCompanies.add(shippingCompany.getShippingCompanyName());
        }
        for  (Driver driver : drivers) {
            this.drivers.add(driver.getFirstName());
        }
    }
}
