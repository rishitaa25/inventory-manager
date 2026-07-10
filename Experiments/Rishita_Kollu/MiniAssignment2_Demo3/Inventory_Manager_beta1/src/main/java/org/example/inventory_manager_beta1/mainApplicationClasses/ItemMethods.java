package org.example.inventory_manager_beta1.mainApplicationClasses;

import jakarta.transaction.Transactional;
import org.example.inventory_manager_beta1.DTO.Items.AddItemRequest;
import org.example.inventory_manager_beta1.entities.Item;
import org.example.inventory_manager_beta1.entities.ShippingCompany;
import org.example.inventory_manager_beta1.services.ItemService;
import org.example.inventory_manager_beta1.services.ShippingCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemMethods {
    @Autowired
    private ShippingCompanyService shippingCompanyService;

    @Autowired
    private ItemService itemService;

    /**
     * CREATE: A logic method for creating a new Item
     * @param addItemRequest
     *  The Item DTO containing the Item information from a JSON
     * @return
     *  True if completed
     */
    @PostMapping("/item/add")
    public Boolean addItem (@RequestBody AddItemRequest addItemRequest) {
        ShippingCompany itemCompany = shippingCompanyService.findByShippingCompanyId(addItemRequest.getShipmentCompanyId());
        Item item = new Item(addItemRequest.getItemName(), itemCompany, addItemRequest.getStorageLocation(), addItemRequest.getQuantity(), addItemRequest.getWeight(), addItemRequest.getStorageType(), addItemRequest.getArrivalDate());
        itemService.addNewItem(item);
        return true;
    }

    /**
     * CREATE: A logic method for creating several items at once
     * @param addItemRequests
     *  The Item DTO containing the Item information from a JSON
     * @return
     *  True if completed
     */
    @PostMapping("/items/add")
    @Transactional
    public Boolean addItems (@RequestBody List<AddItemRequest> addItemRequests) {
        for (AddItemRequest addItemRequest : addItemRequests) {
            ShippingCompany itemCompany = shippingCompanyService.findByShippingCompanyId(addItemRequest.getShipmentCompanyId());
            Item item = new Item(addItemRequest.getItemName(), itemCompany, addItemRequest.getStorageLocation(), addItemRequest.getQuantity(), addItemRequest.getWeight(), addItemRequest.getStorageType(), addItemRequest.getArrivalDate());
            itemService.addNewItem(item);
        }
        return true;
    }

    /**
     * READ: A search method for getting all existing items
     * @return
     *  The list of current items
     */
    @GetMapping("/items")
    public List<Item> items() {return itemService.findAll();}

    /**
     * READ: A search method for getting a specific Item
     * @param itemSKU
     *  The SKU of the item
     * @return
     *  The Item
     */
    @GetMapping("/item/{itemSKU}")
    public Item item(@PathVariable Integer itemSKU) {return itemService.findBySKU(itemSKU);}

    /**
     * UPDATE: A logic method for updating an existing Item
     * @param itemSKU
     *  The SQU of the Item
     * @param addItemRequest
     *  The Item DTO containing the new Item information from a JSON
     * @return
     *  True or False
     */
    @PutMapping("/item/{itemSKU}/update")
    public Boolean updateItem(@PathVariable Integer itemSKU, @RequestBody AddItemRequest addItemRequest) {
        Item updateItem = itemService.findBySKU(itemSKU);
        ShippingCompany itemCompany = shippingCompanyService.findByShippingCompanyId(addItemRequest.getShipmentCompanyId());
        updateItem.setItemName(addItemRequest.getItemName());
        updateItem.setShippingCompany(itemCompany);
        updateItem.setStorageLocation(addItemRequest.getStorageLocation());
        updateItem.setQuantity(addItemRequest.getQuantity());
        updateItem.setWeight(addItemRequest.getWeight());
        updateItem.setStorageType(addItemRequest.getStorageType());
        updateItem.setArrivalDate(addItemRequest.getArrivalDate());
        itemService.addNewItem(updateItem);
        return true;
    }

    /**
     * DELETE: A logic method for deleting a specific Item
     * @param skuId
     *  The skuId of the item
     * @return
     *  True or False
     */
    @DeleteMapping("/delete/item/{skuId}")
    public Boolean deleteItem(@PathVariable Integer skuId) {
        itemService.deleteById(skuId);
        return true;
    }

    /**
     * DELETE: A logic method for deleting all items
     * @return
     *  True if completed
     */
    @DeleteMapping("/item/deleteAll")
    public Boolean deleteAllItems() {
        for (Item item : itemService.findAll()) {
            itemService.deleteById(item.getSkuId());
        }
        return true;
    }
}
