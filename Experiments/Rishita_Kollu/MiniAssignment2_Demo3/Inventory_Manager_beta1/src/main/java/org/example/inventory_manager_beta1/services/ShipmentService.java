package org.example.inventory_manager_beta1.services;

import jakarta.transaction.Transactional;
import org.example.inventory_manager_beta1.DTO.Items.AddItemRequest;
import org.example.inventory_manager_beta1.DTO.Shipments.ShipmentRequest;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.ShipmentNotFoundException;
import org.example.inventory_manager_beta1.entities.*;
import org.example.inventory_manager_beta1.mainApplicationClasses.WebSocket.WS_ShipmentNotification;
import org.example.inventory_manager_beta1.repositories.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShipmentService {
    @Autowired
    private final ShipmentRepository shipmentRepository;

    /**
     * INIT:
     * @param shipmentRepository
     *
     */
    public ShipmentService(ShipmentRepository shipmentRepository) {this.shipmentRepository = shipmentRepository;}

    @Autowired
    private AdminService adminService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private ShippingCompanyService shippingCompanyService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryChangeService inventoryChangeService;

    @Autowired
    private WS_ShipmentNotification wsShipmentNotification;

    /*==================================SEARCH METHODS==================================*/

    /**
     * A search method to find a specific shipment by shipmentId
     * @param shipmentId
     *  The ID number of the shipment
     * @return
     *  The shipment object
     */
    public Shipment findByShipmentId(Integer shipmentId) {
        return shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException(STR."Shipment with ID \{shipmentId} not found"));
    }

    /**
     * A search method to return all the shipments in the database
     * @return
     *  A list of all existing shipments
     */
    public List<Shipment> findAll() {return shipmentRepository.findAll();}

    /**
     *
     * @param employeeId
     * @return
     */
    public List<Shipment> findByDrivers(Integer employeeId) {return shipmentRepository.findByDrivers_EmployeeId(employeeId);}

    /**
     * A search method that returns all shipments that have arrived
     * @param arrived
     *  A boolean parameter of whether the shipment has arrived or not
     * @return
     *  A list of all shipments that have arrived or not
     */
    public List<Shipment> findByArrived(Boolean arrived) {return shipmentRepository.findByArrived(arrived);}

    /**
     * A search method that returns all shipments that are supposed to arrive on a certain day
     * @param deliveryDay
     *  The LocalDate date the shipment is supposed to arrive
     * @return
     *  A list of all shipments made on that day
     */
    public List<Shipment> findByDeliveryDate(LocalDate deliveryDay) {return shipmentRepository.findByDeliveryDate(deliveryDay);}

    /*==================================LOGIC METHODS==================================*/

    /**
     * A logic method that creates a new Shipment object
     * @param shipmentRequest
     *  The Shipment object being created
     */
    @Transactional
    public void addNewShipment(ShipmentRequest shipmentRequest) {
        Shipment requestedShipment = new Shipment(shipmentRequest.getRequestedDeliveryDate());

        //Admin that created the shipment request
        Admin creationAdmin = adminService.findById(shipmentRequest.getId());
        requestedShipment.setCreationAdmin(creationAdmin);

        //Adding items to Shipment
        requestedShipment.setItems(new ArrayList<>());
        for (AddItemRequest itemRequest : shipmentRequest.getItems()) {
            ShippingCompany company = null;
            if (itemRequest.getShipmentCompanyId() != null) {
                company = shippingCompanyService.findByShippingCompanyId(itemRequest.getShipmentCompanyId());
            } else {
                company = getRandomCompany();
            }

            Item item = new Item(
                    itemRequest.getItemName(),
                    company,
                    itemRequest.getStorageLocation(),
                    itemRequest.getQuantity(),
                    itemRequest.getWeight(),
                    itemRequest.getStorageType(),
                    itemRequest.getArrivalDate()
            );
            item.setShipment(requestedShipment);
            requestedShipment.getItems().add(item);
        }

        //Adding Drivers to Shipment
        List<Driver> drivers = driverService.findAllByDriverId(shipmentRequest.getDriverIds());
        requestedShipment.setDrivers(drivers);

        //Adding Shipping Companies
        List<ShippingCompany> companies = requestedShipment.getItems().stream()
                .map(Item::getShippingCompany)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        requestedShipment.setShippingCompanies(companies);

        shipmentRepository.save(requestedShipment);

        //Notifying the admin that made the shipment request
        wsShipmentNotification.notifyUser(creationAdmin.getUserName(),
                STR."Your shipment request #\{requestedShipment.getShipmentId()} has been created successfully");

        //Notifying all drivers that were assigned to the shipment
        for (Driver driver : drivers) {
            wsShipmentNotification.notifyUser(driver.getUserName(),
                    STR."You have been assigned to shipment #\{requestedShipment.getShipmentId()}");
        }
    }

    /**
     *
     * @param shipmentId
     * @param shipmentRequest
     */
    @Transactional
    public void updateShipment(Integer shipmentId,ShipmentRequest shipmentRequest) {
        Shipment updatedShipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
        updatedShipment.setDeliveryDate(shipmentRequest.getRequestedDeliveryDate());
        List<Driver> drivers = driverService.findAllByDriverId(shipmentRequest.getDriverIds());
        updatedShipment.setDrivers(drivers);
        shipmentRepository.save(updatedShipment);
    }

    /**
     * A logic method that assigns a Driver object to a Shipment based on shipmentId
     * @param driverId
     *  The ID of the Driver being assigned
     * @param shipmentId
     *  The ID of the shipment that is being assigned a driver
     */
    public void assignDriver(Integer driverId, Integer shipmentId) {
        Shipment shipment = findByShipmentId(shipmentId);
        Driver driver = driverService.findByDriverId(driverId);
        shipment.getDrivers().add(driver);
        shipmentRepository.save(shipment);
    }

    /**
     * A logic method that changes the shipments arrival status from false to true
     * @param shipmentId
     *  The ID of the shipment that has arrived
     */
    public void arrived(Integer shipmentId) {
        Shipment shipment = findByShipmentId(shipmentId);
        shipment.setArrived(true);
        shipmentRepository.save(shipment);
    }

    /**
     * A logic method that simulates the offloading of a shipment by adding all the items into the inventory
     * @param shipmentId
     *  The ID of the shipment
     */
    @Transactional
    public void offloadShipment(Integer shipmentId) {
        Shipment shipment = findByShipmentId(shipmentId);
        if (shipment.getOffloaded()) {
            throw new ShipmentNotFoundException(STR."Shipment #\{shipmentId} has already been offloaded");
        }

        for (Item item : shipment.getItems()) {
            Optional<Inventory> existingItem = inventoryService.findByItemName(item.getItemName());

            if (existingItem.isPresent()) {
                // Item already exists — update quantity and log the change
                Inventory inventory = existingItem.get();
                int previousAmount = inventory.getAmountOfItem();
                inventory.setAmountOfItem(previousAmount + item.getQuantity());
                inventoryService.saveItem(inventory);

                // Log the change
                InventoryChange change = new InventoryChange(
                        inventory.getSkuNumber(),
                        item.getQuantity(),
                        shipment.getCreationAdmin().getEmployeeId()
                );
                inventoryChangeService.addChange(change);

            } else {
                // Item doesn't exist — create new inventory entry
                inventoryService.saveItem(new Inventory(
                        item.getItemName(),
                        item.getQuantity()
                ));
            }
        }

        shipment.setOffloaded(true);
        shipmentRepository.save(shipment);
    }

    /**
     * A logic method that will delete a shipment from the inventory
     * @param shipmentId
     *  The ID of the shipment
     */
    public void deleteShipment(Integer shipmentId) {
        Shipment shipment = findByShipmentId(shipmentId);
        shipmentRepository.delete(shipment);
    }

    private ShippingCompany getRandomCompany() {
        List<ShippingCompany> companies = shippingCompanyService.findAll();
        if (!companies.isEmpty()) {
            Random random = new Random();
            return companies.get(random.nextInt(companies.size()));
        }
        return null;
    }
}
