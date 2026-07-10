package org.example.inventory_manager_beta1.MainApplicationClasses;

import jakarta.transaction.Transactional;
import org.example.inventory_manager_beta1.DTO.Items.Add.AddItemRequest;
import org.example.inventory_manager_beta1.DTO.Items.Search.SearchItemResponse;
import org.example.inventory_manager_beta1.DTO.Items.Update.UpdateItemRequest;
import org.example.inventory_manager_beta1.DataEnums.StorageType;
import org.example.inventory_manager_beta1.Entities.Item;
import org.example.inventory_manager_beta1.Services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * A controller class containing all the different HTTP mappings for Item related methods
 */
@RestController
@RequestMapping("/item")
public class ItemController {
    /**
     * The auto-injected itemService class for handling Item related logic
     */
    @Autowired
    private ItemService itemService;

    /**
     * CREATE: A controller method for handling Item creation
     * @param addItemRequest
     *  The DTO containing the Item information from a JSON
     * @return
     *  A message confirming if the Item was created
     */
    @PostMapping("/add")
    public String addItem (@RequestBody AddItemRequest addItemRequest) {return itemService.addNewItem(addItemRequest).getMessage();}

    /**
     * CREATE: A logic method for creating several items at once
     * @param addItemRequests
     *  The Item DTO containing the Item information from a JSON
     * @return
     *  True if completed
     */
    @PostMapping("/multi-add")
    @Transactional
    public Boolean addItems (@RequestBody List<AddItemRequest> addItemRequests) {
        for (AddItemRequest addItemRequest : addItemRequests) {
            itemService.addNewItem(addItemRequest);
        }
        return true;
    }

    /**
     * READ: A search method for getting a specific Item
     * @param itemSKU
     *  The SKU of the item
     * @return
     *  The Item with the matching SKU if it exists
     */
    @GetMapping("/find-by/sku/{itemSKU}")
    public Item getItemViaSKU(@PathVariable("itemSKU") Integer itemSKU) {return itemService.findBySKU(itemSKU);}

    /**
     * READ: A search method for getting details of a specific Item
     * @param itemSKU
     *  The SKU of the item
     * @return
     *  A message with details about the Item
     */
    @GetMapping("/find-by/sku/{itemSKU}/details")
    public SearchItemResponse getItemDetailViaSKU(@PathVariable("itemSKU") Integer itemSKU) {return itemService.findDetailsById(itemSKU);}

    /**
     * READ: A search method for getting a specific Item
     * @param itemName
     *  The name of the item
     * @return
     *  A List of Item(s) with a matching name
     */
    @GetMapping("/find-by/name/{itemName}")
    public List<Item> getItemViaName(@PathVariable("itemName") String itemName) {return itemService.findByItemName(itemName);}

    /**
     * READ: A search method for getting a specific Item
     * @param itemQuantity
     *  The quantity of the item
     * @return
     *  A List of Item(s) with a matching quantity or greater
     */
    @GetMapping("/find-by/quantity/{itemQuantity}")
    public List<Item> getItemViaQuantity(@PathVariable("itemQuantity") Integer itemQuantity) {return itemService.findByQuantity(itemQuantity);}

    /**
     * READ: A search method for getting a specific Item
     * @param itemWeight
     *  The weight of the item
     * @return
     *  A List of Item(s) with a matching weight or greater
     */
    @GetMapping("/find-by/weight/{itemWeight}")
    public List<Item> getItemViaWeight(@PathVariable("itemWeight") Double itemWeight) {return itemService.findByWeight(itemWeight);}

    /**
     * READ: A search method for getting a specific Item
     * @param itemStorageType
     *  The storage type of the item
     * @return
     *  A List of Item(s) with a matching storage type
     */
    @GetMapping("/find-by/storage-type/{itemStorageType}")
    public List<Item> getItemViaStorageType(@PathVariable("itemStorageType") StorageType itemStorageType) {return itemService.findByStorageType(itemStorageType);}

    /**
     * READ: A search method for getting all existing items
     * @return
     *  The list of current items
     */
    @GetMapping("/find/all")
    public List<Item> getItems() {return itemService.findAll();}

    /**
     * UPDATE: A logic method for updating an existing Item
     * @param updateItemRequest
     *  The DTO containing the new Item information from a JSON
     * @return
     *  A message confirming a successful information update
     */
    @PutMapping("/update")
    public String updateItem(@RequestBody UpdateItemRequest updateItemRequest) {return itemService.updateItem(updateItemRequest).getMessage();}

    /**
     * UPDATE: A logic method for increasing the quantity of an existing Item
     * @param itemSKU
     *  The SKU of the Item
     * @param quantity
     *  The quantity being added to the existing Item
     * @return
     *  A status message containing details about the Item's new quantity
     */
    @Transactional
    @PatchMapping("/{itemSKU}/quantity/increase/{quantity}")
    public String increaseQuantity(@PathVariable("itemSKU") Integer itemSKU, @PathVariable("quantity") Integer quantity) {return itemService.increaseItemQuantity(itemSKU, quantity);}

    /**
     * UPDATE: A logic method for decreasing the quantity of an existing Item
     * @param itemSKU
     *  The SKU of the Item
     * @param quantity
     *  The quantity being added to the existing Item
     * @return
     *  A status message containing details about the Item's new quantity
     */
    @Transactional
    @PatchMapping("/{itemSKU}/quantity/decrease/{quantity}")
    public String decreaseQuantity(@PathVariable("itemSKU") Integer itemSKU, @PathVariable("quantity") Integer quantity) {return itemService.decreaseItemQuantity(itemSKU, quantity);}


    /**
     * DELETE: A logic method for deleting a specific Item
     * @param skuId
     *  The skuId of the item
     * @return
     *  True if completed
     */
    @DeleteMapping("/delete/{skuId}")
    public Boolean deleteItem(@PathVariable Integer skuId) {
        itemService.deleteById(skuId);
        return true;
    }

    /**
     * DELETE: A logic method for deleting all items
     * @return
     *  True if completed
     */
    @DeleteMapping("/delete/all")
    public Boolean deleteAllItems() {
        itemService.deleteAll();
        return true;
    }
}
