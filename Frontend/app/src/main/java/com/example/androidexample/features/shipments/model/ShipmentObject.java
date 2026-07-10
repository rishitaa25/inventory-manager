package com.example.androidexample.features.shipments.model;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ShipmentObject  implements Serializable {

    private Integer shipmentId;

    private LocalDate deliveryDate;

    private Boolean arrived;

    private Boolean offloaded;

    private List<ShipmentItemObject> items;

    private List<String> shippingCompanies;

    private List<String> drivers;

    private String creationAdmin;

    public ShipmentObject(Integer shipmentId, LocalDate deliveryDate, Boolean arrived, Boolean offloaded,
                          List<ShipmentItemObject> items, String creationAdmin) {

        this.shipmentId = shipmentId;
        this.deliveryDate = deliveryDate;
        this.arrived = arrived;
        this.offloaded = offloaded;
        this.items = items;
        this.creationAdmin = creationAdmin;
    }

    public ShipmentObject (Integer shipmentId, LocalDate deliveryDate, Boolean arrived, Boolean offloaded){
        this.shipmentId = shipmentId;
        this.deliveryDate = deliveryDate;
        this.arrived = arrived;
        this.offloaded = offloaded;
    }

    public ShipmentObject(int shipmentId, LocalDate deliveryDate, boolean arrived, boolean offloaded, List<ShipmentItemObject> items) {
        this.shipmentId = shipmentId;
        this.deliveryDate = deliveryDate;
        this.arrived = arrived;
        this.offloaded = offloaded;
        this.items = items;
    }

    public ShipmentObject(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public Boolean getArrived() {
        return arrived;
    }

    public Boolean getOffloaded() {
        return offloaded;
    }
    public List<ShipmentItemObject> getItems() {
        return items;
    }

    public String getCreationAdmin() {
        return creationAdmin;
    }
}
