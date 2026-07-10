package com.example.androidexample.features.shipments.data;

import com.example.androidexample.features.shipments.model.ShipmentObject;

import java.util.List;

public class ShipmentRepository {
    public static List<ShipmentObject> shipments;

    public static void setShipments(List<ShipmentObject> newShipments) {
       shipments = newShipments;
    }

    public static List<ShipmentObject> getShipments() {
        return shipments;
    }

    public static void clearShipments() {
        shipments.clear();
    }
}
