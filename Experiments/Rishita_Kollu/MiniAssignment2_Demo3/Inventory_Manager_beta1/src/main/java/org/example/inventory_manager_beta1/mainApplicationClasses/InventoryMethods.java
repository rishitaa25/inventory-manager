package org.example.inventory_manager_beta1.mainApplicationClasses;

import org.example.inventory_manager_beta1.DTO.Inventories.AddInventoryRequest;
import org.example.inventory_manager_beta1.DTO.Inventories.InventoryChangeRequest;
import org.example.inventory_manager_beta1.entities.Inventory;
import org.example.inventory_manager_beta1.entities.InventoryChange;
import org.example.inventory_manager_beta1.services.InventoryChangeService;
import org.example.inventory_manager_beta1.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InventoryMethods {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryChangeService changeServices;

    /**
     * POST Add inventory item
     * {
     *   "skuNumber": 101,
     *   "itemName": "Milk",
     *   "itemDescription": "1 gallon milk",
     *   "amountOfItem": 20
     * }
     */
    @PostMapping("/inventory/add")
    public Boolean addInventory(@RequestBody AddInventoryRequest request){

        Inventory item = new Inventory(
                request.getItemName(),
                request.getItemDescription(),
                request.getAmountOfItem()
        );

        inventoryService.saveItem(item);
        return true;
    }

    /**
     * GET All inventory
     */
    @GetMapping("/inventory")
    public List<Inventory> getInventory(){
        return inventoryService.findAll();
    }

    /**
     * GET Single item
     */
    @GetMapping("/inventory/{sku}")
    public Inventory getItem(@PathVariable Integer sku){
        return inventoryService.findBySku(sku);
    }

    /**
     * PUT Update amount + store history
     * {
     *   "changeID": 1,
     *   "skuNumber": 101,
     *   "amountOfItem": 15,
     *   "employeeID": 234
     * }
     */
    @PutMapping("/inventory/update")
    public Boolean updateInventory(@RequestBody InventoryChangeRequest request){

        Inventory item = inventoryService.findBySku(request.getSkuNumber());

        item.setAmountOfItem(item.getAmountOfItem() + request.getAmountOfItem());
        inventoryService.saveItem(item);

        InventoryChange change = new InventoryChange(
                request.getSkuNumber(),
                request.getAmountOfItem(),
                request.getEmployeeId()
        );

        changeServices.addChange(change);
        return true;
    }

    /**
     * DELETE inventory item
     */
    @DeleteMapping("/inventory/delete/{sku}")
    public Boolean deleteItem(@PathVariable Integer sku){
        inventoryService.deleteItem(sku);
        return true;
    }

    /**
     * GET change history
     */
    @GetMapping("/inventory/changes")
    public List<InventoryChange> getChanges(){
        return changeServices.findAll();
    }
}
