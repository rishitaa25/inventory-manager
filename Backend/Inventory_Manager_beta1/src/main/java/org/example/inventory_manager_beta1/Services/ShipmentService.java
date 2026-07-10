package org.example.inventory_manager_beta1.Services;

import org.example.inventory_manager_beta1.DTO.Items.Add.AddItemRequest;
import org.example.inventory_manager_beta1.DTO.Shipments.ShipmentRequest;
import org.example.inventory_manager_beta1.DTO.Shipments.ShipmentResponse;
import org.example.inventory_manager_beta1.DTO.Shipments.ShipmentSearchResponse;
import org.example.inventory_manager_beta1.DTO.Shipments.ShipmentUpdateRequest;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.IllegalActionException;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.ShipmentNotFoundException;
import org.example.inventory_manager_beta1.Entities.*;
import org.example.inventory_manager_beta1.MainApplicationClasses.WebSockets.WS_ShipmentNotification;
import org.example.inventory_manager_beta1.Repositories.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A robust service class to handle all the different logic and search methods relating
 * to the Shipment class. It is complete with all the ShipmentRepository search methods
 * defined, and it handles every Shipment related logic method with included custom error handling
 */
@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    @Autowired
    @Lazy
    private NotificationService notificationService;
    private final WS_ShipmentNotification wsShipmentNotification;

    @Autowired
    private AdminService adminService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryChangeService inventoryChangeService;

    /**
     * INIT: Constructs the service with its required dependencies.
     * @param shipmentRepository
     *  The repository containing custom Shipment search methods
     * @param wsShipmentNotification
     *  WebSocket endpoint for pushing real-time shipment alerts
     */
    public ShipmentService(ShipmentRepository shipmentRepository, WS_ShipmentNotification wsShipmentNotification) {
        this.shipmentRepository = shipmentRepository;
        this.wsShipmentNotification = wsShipmentNotification;
    }
    /*==================================SEARCH METHODS==================================*/

    /**
     * SEARCH: A search method to find a specific Shipment by id
     * @param id
     *  The ID number of the Shipment
     * @return
     *  The Shipment object if it exists
     */
    public Shipment findById(Integer id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with ID: " +  id + " not found"));
    }

    /**
     * SEARCH: A search method for finding all Shipments in the database
     * @return
     *  A list of all existing Shipments in the database
     */
    @Transactional
    public List<ShipmentSearchResponse> findAll() {
        return shipmentRepository.findAll().stream()
                .map(this::toSearchResponse)
                .collect(Collectors.toList());
    }

    /**
     * SEARCH: A search method for finding all Shipments assigned to a Driver
     * using their employee identification number
     * @param employeeId
     *  The employee identification number of the Driver being searched for
     * @return
     *  A list of all Shipments assigned to the Driver with the matching employee ID
     */
    @Transactional
    public List<ShipmentSearchResponse> findByDrivers(Integer employeeId) {
        return shipmentRepository.findByDrivers_EmployeeId(employeeId).stream()
                .map(this::toSearchResponse)
                .collect(Collectors.toList());
    }

    /**
     * SEARCH: A search method for finding all Shipments matching a given arrival status
     * @param arrived
     *  The arrival status of the Shipment(s) being searched for
     * @return
     *  A list of all Shipments with a matching arrival status
     */
    @Transactional
    public List<ShipmentSearchResponse> findByArrived(Boolean arrived) {
        return shipmentRepository.findByArrived(arrived).stream()
                .map(this::toSearchResponse)
                .collect(Collectors.toList());
    }

    /**
     * SEARCH: A search method for finding all Shipments scheduled for a given delivery date
     * @param deliveryDay
     *  The delivery date of the Shipment(s) being searched for
     * @return
     *  A list of all Shipments with a matching delivery date
     */
    @Transactional
    public List<ShipmentSearchResponse> findByDeliveryDate(LocalDate deliveryDay) {
        return shipmentRepository.findByArrivalDate(deliveryDay).stream()
                .map(this::toSearchResponse)
                .collect(Collectors.toList());
    }

    /*==================================LOGIC METHODS==================================*/

    /**
     * CREATE: A logic method that creates a new Shipment object, assigns the creation
     * Admin, items, drivers, and shipping companies, saves it to the database, and
     * notifies all relevant users via WebSocket
     * @param shipmentRequest
     *  The DTO containing all the information for the new Shipment
     */
    @Transactional
    public ShipmentResponse addNewShipment(ShipmentRequest shipmentRequest) {
        Shipment requestedShipment = new Shipment(shipmentRequest.getRequestedDeliveryDate());

        Admin creationAdmin = adminService.findById(shipmentRequest.getId());
        requestedShipment.setCreationAdmin(creationAdmin);
        shipmentRepository.save(requestedShipment);

        // Add items to shipment
        requestedShipment.setItems(new ArrayList<>());
        for (AddItemRequest itemRequest : shipmentRequest.getItems()) {
            Item item = itemService.addNewItem(itemRequest).getItem();
            itemService.saveItem(item);
            item.setShipment(requestedShipment);
            requestedShipment.getItems().add(item);
        }

        // Add drivers to shipment
        List<Driver> drivers = shipmentRequest.getDriverIds() != null
                ? driverService.findAllById(shipmentRequest.getDriverIds())
                : new ArrayList<>();
        requestedShipment.setDrivers(drivers);

        // Add shipping companies derived from items
        List<ShippingCompany> companies = requestedShipment.getItems().stream()
                .map(Item::getShippingCompany)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        requestedShipment.setShippingCompanies(companies);

        shipmentRepository.save(requestedShipment);

        String adminMessage = "Your shipment request #" + requestedShipment.getShipmentId() + " has been created successfully.";
        wsShipmentNotification.notifyUser(creationAdmin.getUserName(), adminMessage);
        notificationService.createGeneralNotification(creationAdmin.getAccessLevel(), adminMessage, requestedShipment.getArrivalDate());

        for (Driver driver : drivers) {
            String driverMessage = "You have been assigned to shipment #" + requestedShipment.getShipmentId() + ".";
            wsShipmentNotification.notifyUser(driver.getUserName(), driverMessage);
            notificationService.createGeneralNotification(driver.getAccessLevel(), driverMessage, requestedShipment.getArrivalDate());
        }

        return new ShipmentResponse(
                requestedShipment.getShipmentId(),
                requestedShipment.getCreationAdmin().getEmployeeId(),
                requestedShipment.getItems(),
                requestedShipment.getArrivalDate(),
                requestedShipment.getArrived(),
                requestedShipment.getOffloaded()
        );
    }

    /**
     * UPDATE: A logic method that updates an existing Shipment's delivery date and
     * assigned drivers
     * @param shipmentUpdateRequest
     *  The DTO containing the updated information for the Shipment
     */
    @Transactional
    public void updateShipment(ShipmentUpdateRequest shipmentUpdateRequest) {
        Shipment updatedShipment = findById(shipmentUpdateRequest.getShipmentId());
        if (shipmentUpdateRequest.getArrivalDate() != null)
            updatedShipment.setArrivalDate(shipmentUpdateRequest.getArrivalDate());
        updatedShipment.setArrived(shipmentUpdateRequest.isArrived());
        updatedShipment.setOffloaded(shipmentUpdateRequest.isOffloaded());
        shipmentRepository.save(updatedShipment);
    }

    /**
     * UPDATE: A logic method that assigns a Driver to an existing Shipment
     * @param driverId
     *  The identification number of the Driver being assigned
     * @param shipmentId
     *  The identification number of the Shipment being assigned a Driver
     */
    public void assignDriver(Integer driverId, Integer shipmentId) {
        Shipment shipment = findById(shipmentId);
        Driver driver = driverService.findById(driverId);
        shipment.getDrivers().add(driver);
        shipmentRepository.save(shipment);

        String message = "You have been assigned to shipment #" + shipmentId + ".";
        wsShipmentNotification.notifyUser(driver.getUserName(), message);
        notificationService.createGeneralNotification(driver.getAccessLevel(), message, shipment.getArrivalDate());
    }

    /**
     * UPDATE: A logic method that marks a Shipment as arrived by setting its
     * arrived flag to true
     * @param shipmentId
     *  The identification number of the Shipment that has arrived
     */
    public void arrived(Integer shipmentId) {
        Shipment shipment = findById(shipmentId);
        shipment.setArrived(true);
        shipmentRepository.save(shipment);

        // Notify the creation admin
        String adminMessage = "Shipment #" + shipmentId + " has arrived and is ready to be offloaded.";
        wsShipmentNotification.notifyUser(shipment.getCreationAdmin().getUserName(), adminMessage);
        notificationService.createGeneralNotification(shipment.getCreationAdmin().getAccessLevel(), adminMessage, LocalDate.now());

        // Notify all assigned drivers
        for (Driver driver : shipment.getDrivers()) {
            String driverMessage = "Shipment #" + shipmentId + " has arrived. Please prepare for offloading.";
            wsShipmentNotification.notifyUser(driver.getUserName(), driverMessage);
            notificationService.createGeneralNotification(driver.getAccessLevel(), driverMessage, LocalDate.now());
        }
    }

    /**
     * UPDATE: A logic method that offloads a Shipment by adding all of its Items
     * into the Inventory, logging each change, and marking the Shipment as offloaded
     * @param shipmentId
     *  The identification number of the Shipment being offloaded
     * @throws IllegalActionException
     *  Custom RuntimeException if the Shipment has already been offloaded or hasn't arrived yet
     */
    @Transactional
    public void offloadShipment(Integer shipmentId) {
        Shipment shipment = findById(shipmentId);

        if (!shipment.getArrived())
            throw new IllegalActionException("Shipment #" + shipmentId + " has not arrived yet.");

        if (shipment.getOffloaded())
            throw new IllegalActionException("Shipment #" + shipmentId + " has already been offloaded.");

        for (Item item : shipment.getItems()) {
            Inventory existingItem = inventoryService.findByItemName(item.getItemName());

            if (existingItem != null) {
                int previousAmount = existingItem.getAmountOfItem();
                existingItem.setAmountOfItem(previousAmount + item.getQuantity());
                inventoryService.saveItem(existingItem);

                InventoryChange change = new InventoryChange(
                        existingItem.getSkuNumber(), item.getQuantity(),
                        shipment.getCreationAdmin().getEmployeeId());
                inventoryChangeService.addChange(change);
            } else {
                inventoryService.saveItem(new Inventory(item.getItemName(), item.getQuantity()));
            }
        }

        shipment.setOffloaded(true);
        shipmentRepository.save(shipment);

        // Notify the creation admin
        String adminMessage = "Shipment #" + shipmentId + " has been fully offloaded and inventory has been updated.";
        wsShipmentNotification.notifyUser(shipment.getCreationAdmin().getUserName(), adminMessage);
        notificationService.createGeneralNotification(shipment.getCreationAdmin().getAccessLevel(), adminMessage, LocalDate.now());

        // Notify all assigned drivers
        for (Driver driver : shipment.getDrivers()) {
            String driverMessage = "Shipment #" + shipmentId + " has been fully offloaded. Thank you!";
            wsShipmentNotification.notifyUser(driver.getUserName(), driverMessage);
            notificationService.createGeneralNotification(driver.getAccessLevel(), driverMessage, LocalDate.now());
        }
    }

    /**
     * DELETE: A logic method that will delete a shipment from the database
     * using its identification number
     * @param shipmentId
     *  The identification number of the shipment being deleted
     */
    public void deleteShipment(Integer shipmentId) {
        Shipment shipment = findById(shipmentId);
        shipmentRepository.delete(shipment);
    }

    /**
     * DELETE: A logic method that will delete all shipments from the database
     */
    public void deleteAllShipments() {shipmentRepository.deleteAll();}

    /*==================================PRIVATE HELPERS==================================*/

    /**
     * HELPER: Converts a {@link Shipment} entity to a {@link ShipmentSearchResponse} DTO.
     * Extracted to avoid repeating the same mapping logic across all search methods.
     * @param shipment
     *  The Shipment entity to convert
     * @return
     *  The corresponding ShipmentSearchResponse DTO
     */
    private ShipmentSearchResponse toSearchResponse(Shipment shipment) {
        return new ShipmentSearchResponse(
                shipment.getShipmentId(),
                shipment.getArrivalDate(),
                shipment.getArrived(),
                shipment.getOffloaded(),
                shipment.getItems(),
                shipment.getShippingCompanies(),
                shipment.getDrivers(),
                shipment.getCreationAdmin() != null ? shipment.getCreationAdmin().getUserName() : null
        );
    }
}
