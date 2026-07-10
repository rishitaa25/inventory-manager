package com.example.androidexample.features.shipments.model;

//import com.example.androidexample.SearchItem;
import com.example.androidexample.features.shipments.enums.StorageType;

import java.io.Serializable;
import java.time.LocalDate;

public class ShipmentItemObject implements Serializable
    {

    private Integer skuId;
    private String itemName;
    private String shippingCompany;
    private ShipmentObject shipment;
    private String storageLocation;
    private Integer quantity;
    private Double weight;
    private LocalDate arrivalDate;
    private StorageType storageType;

    public ShipmentItemObject() {}

    public ShipmentItemObject(Integer skuId, String itemName, String shippingCompany, ShipmentObject shipment,
                              String storageLocation, Integer quantity, Double weight, LocalDate arrivalDate, String storageType) {
        this.skuId = skuId;
        this.itemName = itemName;
        this.shippingCompany = shippingCompany;
        this.shipment = shipment;
        this.storageLocation = storageLocation;
        this.quantity = quantity;
        this.weight = weight;
        this.arrivalDate = arrivalDate;
        this.storageType = StorageType.valueOf(storageType);
    }

    public ShipmentItemObject(int itemId, String name, int quantity, String location) {
        this.skuId = itemId;
        this.itemName = name;
        this.quantity = quantity;
        this.storageLocation = location;
    }

    public ShipmentItemObject(String itemName, String storageLocation, int quantityInt, int weightInt, String storageType)
    {
        this.itemName = itemName;
        this.storageLocation = storageLocation;
        this.quantity = quantityInt;
        this.weight = (double) weightInt;
        this.storageType = StorageType.valueOf(storageType);
    }

        public ShipmentItemObject(int id, String itemName, String shippingCompany, ShipmentObject shipment, String storageLocation, int quantity, double weight, String storageType)
        {
            this.skuId = id;
            this.itemName = itemName;
            this.shippingCompany = shippingCompany;
            this.shipment = shipment;
            this.storageLocation = storageLocation;
            this.quantity = quantity;
            this.weight = weight;
            this.storageType = StorageType.valueOf(storageType);
        }

        public Integer getSkuId() {
    return skuId;
}

    public String getItemName() {
        return itemName;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public ShipmentObject getShipment() {
        return shipment;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public StorageType getStorageType() {return storageType;}

    public Integer getQuantity() {
        return quantity;
    }

    public Double getWeight() {
        return weight;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }
}