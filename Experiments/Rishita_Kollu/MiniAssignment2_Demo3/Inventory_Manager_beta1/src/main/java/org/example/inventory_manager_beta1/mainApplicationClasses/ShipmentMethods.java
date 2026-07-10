package org.example.inventory_manager_beta1.mainApplicationClasses;

import org.example.inventory_manager_beta1.DTO.Shipments.ShipmentRequest;
import org.example.inventory_manager_beta1.entities.Shipment;
import org.example.inventory_manager_beta1.services.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShipmentMethods {
    @Autowired
    private ShipmentService shipmentService;

    /**
     * CREATE: A logic method for creating a new shipment
     * @param shipmentRequest
     *  The Shipment DTO containing the Shipment information from a JSON
     * @return
     *  True if completed
     */
    @PostMapping("/shipment/add")
    public Boolean addShipment (@RequestBody ShipmentRequest shipmentRequest) {
        shipmentService.addNewShipment(shipmentRequest);
        return true;
    }

    /**
     * READ: A search method that returns all shipments
     * @return
     *  A list of all existing shipments
     */
    @GetMapping("/shipments")
    public List<Shipment> shipments() {return shipmentService.findAll();}

    /**
     * READ: A search method that returns a specific shipment
     * @param shipmentId
     *  The ID of the shipment
     * @return
     *  The shipment object
     */
    @GetMapping("/shipment/{shipmentId}")
    public Shipment shipment (@PathVariable("shipmentId") Integer shipmentId) {
        return shipmentService.findByShipmentId(shipmentId);
    }

    /**
     * UPDATE: A logic method that updates a Shipment
     * @param shipmentId
     *  The ID of the Shipment being updated
     * @param shipmentRequest
     *  The Shipment DTO containing the new Shipment information from a JSON
     * @return
     *  True or False
     */
    @PutMapping("/shipment/{shipmentId}/update")
    public Boolean updateShipment(@PathVariable Integer shipmentId, @RequestBody ShipmentRequest shipmentRequest) {
        shipmentService.updateShipment(shipmentId, shipmentRequest);
        return true;
    }

    /**
     * UPDATE: A logic method that assigns a driver to a shipment
     * @param driverId
     *  The ID of the driver to be assigned
     * @param shipmentId
     *  The ID of the shipment the driver is being assigned to
     * @return
     *  True if completed
     */
    @PatchMapping("/shipment/{shipmentId}/addDriver/{driverId}")
    public Boolean addDriver (@PathVariable Integer driverId, @PathVariable Integer shipmentId) {
        shipmentService.assignDriver(driverId, shipmentId);
        return true;
    }

    /**
     * UPDATE: A logic method that sets the delivery status of a shipment to arrived
     * @param shipmentId
     *  The ID of the shipment that has arrived
     * @return
     *  True if completed
     */
    @PatchMapping("/shipment/{shipmentId}/arrived")
    public Boolean arrivedShipment (@PathVariable("shipmentId") Integer shipmentId) {
        shipmentService.arrived(shipmentId);
        return true;
    }

    /**
     * UPDATE: A logic method that simulates the offloading of a shipment's items into inventory
     * @param shipmentId
     *  The ID of the shipment being offloaded
     * @return
     *  True if completed
     */
    @PatchMapping("/shipment/{shipmentId}/offload")
    public Boolean offloadShipment (@PathVariable("shipmentId") Integer shipmentId) {
        shipmentService.offloadShipment(shipmentId);
        return true;
    }

    /**
     * DELETE: A logic method that deletes a shipment
     * @param shipmentId
     *  The ID of the shipment being deleted
     * @return
     *  True or False
     */
    @DeleteMapping("/shipment/{shipmentId}/delete")
    public Boolean deleteShipment(@PathVariable Integer shipmentId) {
        shipmentService.deleteShipment(shipmentId);
        return true;
    }

    /**
     * DELETE: A logic method that deletes all shipments
     * @return
     *  True if completed
     */
    @DeleteMapping("/shipment/deleteAll")
    public Boolean deleteAllShipments () {
        for (Shipment shipment : shipmentService.findAll()) {
            shipmentService.deleteShipment(shipment.getShipmentId());
        }
        return true;
    }
}
