package org.example.inventory_manager_beta1.MainApplicationClasses;

import org.example.inventory_manager_beta1.DTO.Shipments.ShipmentRequest;
import org.example.inventory_manager_beta1.DTO.Shipments.ShipmentResponse;
import org.example.inventory_manager_beta1.DTO.Shipments.ShipmentSearchResponse;
import org.example.inventory_manager_beta1.DTO.Shipments.ShipmentUpdateRequest;
import org.example.inventory_manager_beta1.Entities.Shipment;
import org.example.inventory_manager_beta1.Services.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * A controller class containing all the different HTTP mappings for Shipment related methods
 */
@RestController
@RequestMapping("/shipment")
public class ShipmentController {
    /**
     * The auto-injected ShipmentService class for handling shipment related logic
     */
    @Autowired
    private ShipmentService shipmentService;

    /**
     * CREATE: A logic method for creating a new shipment
     * @param shipmentRequest
     *  The Shipment DTO containing the Shipment information from a JSON
     * @return
     *  True if completed
     */
    @PostMapping("/add")
    public ShipmentResponse addShipment(@RequestBody ShipmentRequest shipmentRequest) {return shipmentService.addNewShipment(shipmentRequest);}

    /**
     * READ: A search method that returns a specific shipment
     * @param shipmentId
     *  The ID of the shipment
     * @return
     *  The shipment object
     */
    @GetMapping("/find-by/id/{shipmentId}")
    public Shipment getShipmentsViaId(@PathVariable("shipmentId") Integer shipmentId) {return shipmentService.findById(shipmentId);}

    /**
     * READ: A search method that returns a List of Shipments that
     * were driven by a specific Employee
     * @param driverId
     *  The employeeId of the Shipment Driver
     * @return
     *  A List of Shipment(s) that were driven by the specific Driver
     */
    @GetMapping("/find-by/driver/{driverId}")
    public List<ShipmentSearchResponse> getShipmentsViaDriverId(@PathVariable("driverId") Integer driverId) {return shipmentService.findByDrivers(driverId);}

    /**
     * READ: A search method that returns a List of Shipments based on
     * their arrival status
     * @param arrivedStatus
     *  The arrival status of the Shipment
     * @return
     *  A List of Shipment(s) with a matching arrival status
     */
    @GetMapping("/find-by/arrived/{arrivedStatus}")
    public List<ShipmentSearchResponse> getShipmentsViaArrivedStatus(@PathVariable("arrivedStatus") boolean arrivedStatus) {return shipmentService.findByArrived(arrivedStatus);}

    /**
     * READ: A search method that returns a List of Shipments based on
     * their delivery date
     * @param deliveryDate
     *  The delivery date of the Shipment
     * @return
     *  A List of Shipment(s) with a matching delivery date
     */
    @GetMapping("/find-by/date/{deliveryDate}")
    public List<ShipmentSearchResponse> getShipmentsViaDeliveryDate(@PathVariable("deliveryDate")LocalDate deliveryDate) {return shipmentService.findByDeliveryDate(deliveryDate);}

    /**
     * READ: A search method that returns all shipments
     * @return
     *  A list of all existing shipments
     */
    @GetMapping("/find/all")
    public List<ShipmentSearchResponse> shipments() {return shipmentService.findAll();}

    /**
     * UPDATE: A logic method that updates a Shipment
     * @param shipmentUpdateRequest
     *  The Shipment DTO containing the new Shipment information from a JSON
     * @return
     *  True or False
     */
    @PutMapping("/update")
    public Boolean updateShipment(@RequestBody ShipmentUpdateRequest shipmentUpdateRequest) {
        shipmentService.updateShipment(shipmentUpdateRequest);
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
    @PatchMapping("/{shipmentId}/addDriver/{driverId}")
    public Boolean addDriver(@PathVariable Integer driverId, @PathVariable Integer shipmentId) {
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
    @PatchMapping("/{shipmentId}/arrived")
    public Boolean arrivedShipment(@PathVariable("shipmentId") Integer shipmentId) {
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
    @PatchMapping("/{shipmentId}/offload")
    public Boolean offloadShipment(@PathVariable("shipmentId") Integer shipmentId) {
        shipmentService.offloadShipment(shipmentId);
        return true;
    }

    /**
     * DELETE: A logic method that deletes a shipment
     * @param shipmentId
     *  The ID of the shipment being deleted
     * @return
     *  True if completed
     */
    @DeleteMapping("/{shipmentId}/delete")
    public Boolean deleteShipment(@PathVariable Integer shipmentId) {
        shipmentService.deleteShipment(shipmentId);
        return true;
    }

    /**
     * DELETE: A logic method that deletes all shipments
     * @return
     *  True if completed
     */
    @DeleteMapping("/delete/all")
    public Boolean deleteAllShipments () {
        shipmentService.deleteAllShipments();
        return true;
    }
}
