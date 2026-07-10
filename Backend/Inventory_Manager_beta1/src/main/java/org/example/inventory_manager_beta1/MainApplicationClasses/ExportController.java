package org.example.inventory_manager_beta1.MainApplicationClasses;

import org.example.inventory_manager_beta1.DTO.Objects.ShortItem;
import org.example.inventory_manager_beta1.DTO.Shipments.ShipmentSearchResponse;
import org.example.inventory_manager_beta1.Entities.*;
import org.example.inventory_manager_beta1.Repositories.ChatMessageRepository;
import org.example.inventory_manager_beta1.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/export")
public class ExportController {

    @Autowired private InventoryService inventoryService;
    @Autowired private InventoryChangeService inventoryChangeService;
    @Autowired private ItemService itemService;
    @Autowired private ShipmentService shipmentService;
    @Autowired private EmployeeService employeeService;
    @Autowired private AdminService adminService;
    @Autowired private DriverService driverService;
    @Autowired private ShiftService shiftService;
    @Autowired private ShippingCompanyService shippingCompanyService;
    @Autowired private ChatMessageRepository chatMessageRepository;

    private ResponseEntity<String> csvResponse(String filename, String csv) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }

    private String safe(Object value) {
        if (value == null) return "";
        String text = value.toString();
        text = text.replace("\"", "\"\"");
        if (text.contains(",") || text.contains("\n") || text.contains("\"")) {
            return "\"" + text + "\"";
        }
        return text;
    }

    @GetMapping(value = "/inventory", produces = "text/csv")
    public ResponseEntity<String> exportInventory() {
        List<Inventory> inventory = inventoryService.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("skuNumber,itemName,itemDescription,amountOfItem\n");

        for (Inventory item : inventory) {
            csv.append(safe(item.getSkuNumber())).append(",");
            csv.append(safe(item.getItemName())).append(",");
            csv.append(safe(item.getItemDescription())).append(",");
            csv.append(safe(item.getAmountOfItem())).append("\n");
        }

        return csvResponse("inventory.csv", csv.toString());
    }

    @GetMapping(value = "/inventory-changes", produces = "text/csv")
    public ResponseEntity<String> exportInventoryChanges() {
        List<InventoryChange> changes = inventoryChangeService.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("changeId,skuNumber,amountOfItem,employeeId\n");

        for (InventoryChange change : changes) {
            csv.append(safe(change.getChangeId())).append(",");
            csv.append(safe(change.getSkuNumber())).append(",");
            csv.append(safe(change.getAmountOfItem())).append(",");
            csv.append(safe(change.getEmployeeId())).append("\n");
        }

        return csvResponse("inventory_changes.csv", csv.toString());
    }

    @GetMapping(value = "/items", produces = "text/csv")
    public ResponseEntity<String> exportItems() {
        List<Item> items = itemService.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("skuId,itemName,description,shippingCompany,shipmentId,storageLocation,quantity,weight,storageType,arrivalDate\n");

        for (Item item : items) {
            csv.append(safe(item.getSkuId())).append(",");
            csv.append(safe(item.getItemName())).append(",");
            csv.append(safe(item.getDescription())).append(",");
            csv.append(safe(item.getShippingCompany() != null ? item.getShippingCompany().getShippingCompanyName() : "")).append(",");
            csv.append(safe(item.getShipment() != null ? item.getShipment().getShipmentId() : "")).append(",");
            csv.append(safe(item.getStorageLocation())).append(",");
            csv.append(safe(item.getQuantity())).append(",");
            csv.append(safe(item.getWeight())).append(",");
            csv.append(safe(item.getStorageType())).append(",");
            csv.append(safe(item.getArrivalDate())).append("\n");
        }

        return csvResponse("items.csv", csv.toString());
    }

    @GetMapping(value = "/shipments", produces = "text/csv")
    public ResponseEntity<String> exportShipments() {
        List<ShipmentSearchResponse> shipments = shipmentService.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("shipmentId,arrivalDate,arrived,offloaded,items,shippingCompanies,drivers,creationAdmin\n");

        for (ShipmentSearchResponse shipment : shipments) {
            String itemNames = shipment.getItems().stream()
                    .map(ShortItem::getItemName)
                    .collect(Collectors.joining("; "));

            String shippingCompanies = String.join("; ", shipment.getShippingCompanies());
            String drivers = String.join("; ", shipment.getDrivers());

            csv.append(safe(shipment.getShipmentId())).append(",");
            csv.append(safe(shipment.getArrivalDate())).append(",");
            csv.append(safe(shipment.isArrived())).append(",");
            csv.append(safe(shipment.isOffloaded())).append(",");
            csv.append(safe(itemNames)).append(",");
            csv.append(safe(shippingCompanies)).append(",");
            csv.append(safe(drivers)).append(",");
            csv.append(safe(shipment.getCreationAdmin())).append("\n");
        }

        return csvResponse("shipments.csv", csv.toString());
    }

    @GetMapping(value = "/employees", produces = "text/csv")
    public ResponseEntity<String> exportEmployees() {
        List<Employee> employees = employeeService.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("employeeId,firstName,lastName,userName,accessLevel,email,phoneNumber\n");

        for (Employee employee : employees) {
            csv.append(safe(employee.getEmployeeId())).append(",");
            csv.append(safe(employee.getFirstName())).append(",");
            csv.append(safe(employee.getLastName())).append(",");
            csv.append(safe(employee.getUserName())).append(",");
            csv.append(safe(employee.getAccessLevel())).append(",");
            csv.append(safe(employee.getEmail())).append(",");
            csv.append(safe(employee.getPhoneNumber())).append("\n");
        }

        return csvResponse("employees.csv", csv.toString());
    }

    @GetMapping(value = "/admins", produces = "text/csv")
    public ResponseEntity<String> exportAdmins() {
        List<Admin> admins = adminService.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("employeeId,firstName,lastName,userName,accessLevel,email,phoneNumber,managementTitle\n");

        for (Admin admin : admins) {
            csv.append(safe(admin.getEmployeeId())).append(",");
            csv.append(safe(admin.getFirstName())).append(",");
            csv.append(safe(admin.getLastName())).append(",");
            csv.append(safe(admin.getUserName())).append(",");
            csv.append(safe(admin.getAccessLevel())).append(",");
            csv.append(safe(admin.getEmail())).append(",");
            csv.append(safe(admin.getPhoneNumber())).append(",");
            csv.append(safe(admin.getManagementTitle())).append("\n");
        }

        return csvResponse("admins.csv", csv.toString());
    }

    @GetMapping(value = "/drivers", produces = "text/csv")
    public ResponseEntity<String> exportDrivers() {
        List<Driver> drivers = driverService.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("employeeId,firstName,lastName,userName,accessLevel,email,phoneNumber\n");

        for (Driver driver : drivers) {
            csv.append(safe(driver.getEmployeeId())).append(",");
            csv.append(safe(driver.getFirstName())).append(",");
            csv.append(safe(driver.getLastName())).append(",");
            csv.append(safe(driver.getUserName())).append(",");
            csv.append(safe(driver.getAccessLevel())).append(",");
            csv.append(safe(driver.getEmail())).append(",");
            csv.append(safe(driver.getPhoneNumber())).append("\n");
        }

        return csvResponse("drivers.csv", csv.toString());
    }

    @GetMapping(value = "/shifts", produces = "text/csv")
    public ResponseEntity<String> exportShifts() {
        List<Shift> shifts = shiftService.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("shiftId,employeeId,employeeUserName,shiftDate,startTime,endTime,duration\n");

        for (Shift shift : shifts) {
            Employee employee = shift.getEmployee();

            csv.append(safe(shift.getId())).append(",");
            csv.append(safe(employee != null ? employee.getEmployeeId() : "")).append(",");
            csv.append(safe(employee != null ? employee.getUserName() : "")).append(",");
            csv.append(safe(shift.getShiftDate())).append(",");
            csv.append(safe(shift.getStartTime())).append(",");
            csv.append(safe(shift.getEndTime())).append(",");
            csv.append(safe(shift.getDuration())).append("\n");
        }

        return csvResponse("shifts.csv", csv.toString());
    }

    @GetMapping(value = "/shipping-companies", produces = "text/csv")
    public ResponseEntity<String> exportShippingCompanies() {
        List<ShippingCompany> companies = shippingCompanyService.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("shippingCompanyId,shippingCompanyName,shippingCompanyEmail,shippingCompanyPhone\n");

        for (ShippingCompany company : companies) {
            csv.append(safe(company.getShippingCompanyId())).append(",");
            csv.append(safe(company.getShippingCompanyName())).append(",");
            csv.append(safe(company.getShippingCompanyEmail())).append(",");
            csv.append(safe(company.getShippingCompanyPhone())).append("\n");
        }

        return csvResponse("shipping_companies.csv", csv.toString());
    }

    @GetMapping(value = "/chat/{room}", produces = "text/csv")
    public ResponseEntity<String> exportChatHistory(@PathVariable String room) {
        List<ChatMessage> messages = chatMessageRepository.findByRoomOrderByTimestampAsc(room);

        StringBuilder csv = new StringBuilder();
        csv.append("id,room,sender,content,timestamp\n");

        for (ChatMessage message : messages) {
            csv.append(safe(message.getId())).append(",");
            csv.append(safe(message.getRoom())).append(",");
            csv.append(safe(message.getSender())).append(",");
            csv.append(safe(message.getContent())).append(",");
            csv.append(safe(message.getTimestamp())).append("\n");
        }

        return csvResponse("chat_" + room + ".csv", csv.toString());
    }
}
