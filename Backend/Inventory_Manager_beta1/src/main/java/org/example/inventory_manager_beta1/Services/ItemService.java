package org.example.inventory_manager_beta1.Services;

import org.example.inventory_manager_beta1.DTO.Items.Add.AddItemRequest;
import org.example.inventory_manager_beta1.DTO.Items.Add.AddItemResponse;
import org.example.inventory_manager_beta1.DTO.Items.Search.SearchItemResponse;
import org.example.inventory_manager_beta1.DTO.Items.Update.UpdateItemRequest;
import org.example.inventory_manager_beta1.DTO.Items.Update.UpdateItemResponse;
import org.example.inventory_manager_beta1.DataEnums.StorageType;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.IllegalActionException;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.ItemNotFoundException;
import org.example.inventory_manager_beta1.Entities.Item;
import org.example.inventory_manager_beta1.Entities.Shipment;
import org.example.inventory_manager_beta1.Entities.ShippingCompany;
import org.example.inventory_manager_beta1.Repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * A robust service class to handle all the different logic and search methods relating
 * to the Item class. It is complete with all the ItemRepository search methods
 * defined, and it handles every Item related logic method with included custom error handling
 */
@Service
public class ItemService {
    /**
     * The auto-injected ItemRepository for handling custom searches
     */
    @Autowired
    private final ItemRepository itemRepository;

    /**
     * The auto-injected ShippingCompanyService for accessing ShippingCompany methods
     */
    @Autowired
    private ShippingCompanyService shippingCompanyService;

    /**
     * INIT: Used for initializing the itemRepository and shippingCompanyRepository
     * for custom database searches
     * @param itemRepository
     *  The repository containing the custom Item search methods
     */
    public ItemService(ItemRepository itemRepository) {this.itemRepository = itemRepository;}

    /*======================================================Search function implementation======================================================*/

    /**
     * SEARCH: A search method for finding an Item using its
     * identification number
     * @param id
     *  The identification number of the Item being searched for
     * @return
     *  The Item with the matching ID number if it exists
     * @throws ItemNotFoundException
     *  Custom NotFound exception if the Item ID search returns null
     */
    public Item findById(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item with ID: " + id + " not found"));
    }

    /**
     * SEARCH: A search method for finding details of an Item
     * using its identification number
     * @param id
     *  The identification number of the Item being searched for
     * @return
     *  Details about the Item with the matching ID number
     * @throws ItemNotFoundException
     *  Custom NotFound exception if the Item ID search returns null
     */
    public SearchItemResponse findDetailsById(Integer id) {
        Item searchItem = findById(id);
        return new SearchItemResponse(
                searchItem.getSkuId(),
                searchItem.getItemName(),
                searchItem.getStorageLocation(),
                searchItem.getQuantity(),
                searchItem.getWeight(),
                searchItem.getStorageType(),
                searchItem.getArrivalDate(),
                Optional.ofNullable(searchItem.getShipment()).map(Shipment::getShipmentId),
                Optional.ofNullable(searchItem.getShippingCompany()).map(ShippingCompany::getShippingCompanyName)
        );
    }

    /**
     * A search method that finds all items from the Items table
     * @return
     *  A list of all Items currently in the database
     */
    public List<Item> findAll() {return itemRepository.findAll();}

    /**
     * A search method that finds an item by name
     * @param itemName
     *  The name of the item
     * @return
     *  The item with the corresponding name
     */
    public List<Item> findByItemName(String itemName) {return itemRepository.findByItemName(itemName);}

    /**
     * A search method that finds an item by skuId
     * @param skuId
     *  the skuId of the item
     * @return
     *  The item with the corresponding skuId
     */
    public Item findBySKU(Integer skuId) {
        return itemRepository.findById(skuId)
                .orElseThrow(() -> new ItemNotFoundException("Item with SKU: " + skuId + " not found"));
    }

    /**
     * A search method that finds all items that are stocked at a certain quantity or higher
     * @param quantity
     *  The minimum quantity an item needs to have
     * @return
     *  A list of all items that have the minimum quantity or higher
     */
    public List<Item> findByQuantity(Integer quantity) {return itemRepository.findByQuantityGreaterThanEqual(quantity);}

    /**
     * A search method that finds all items that are stocked at a certain weight or higher
      * @param weight
     *  The minimum weight, in lbs, an item needs to have
     * @return
     *  A list of all items that have the minimum weight or higher
     */
    public List<Item> findByWeight(Double weight) {return itemRepository.findByWeightGreaterThanEqual(weight);}

    /**
     * A search method that finds all items of a certain storage type
     * @param storageType
     *  The storage type of the item
     * @return
     *  A list of all items with that storage type
     */
    public List<Item> findByStorageType(StorageType storageType) {return itemRepository.findByStorageType(storageType);}

    /*======================================================Logic function implementation======================================================*/

    /**
     * A logic method that saves an Item to the database
     * @param item
     *  The Item being saved
     */
    public void saveItem(Item item) {itemRepository.save(item);}

    /**
     * A logic method that adds a new Item to the Items table
     * @param addItemRequest
     *  The DTO containing the Item information
     */
    public AddItemResponse addNewItem(AddItemRequest addItemRequest) {
        Random random = new Random();
        List<ShippingCompany> companies = shippingCompanyService.findAll();
        Item item = new Item(addItemRequest.getItemName(), addItemRequest.getDescription(), addItemRequest.getStorageLocation(), addItemRequest.getQuantity(), addItemRequest.getWeight(), addItemRequest.getStorageType(), addItemRequest.getArrivalDate());
        item.setShippingCompany(companies.get(random.nextInt(companies.size())));
        itemRepository.save(item);
        return new AddItemResponse(item, item.getItemName(), item.getQuantity());
    }

    /**
     * A logic method that updates an existing Item's information
     * @param updateItemRequest
     *  The DTO containing the new information for the Item
     * @return
     *  A DTO containing a status message
     */
    public UpdateItemResponse updateItem(UpdateItemRequest updateItemRequest) {
        Item updateItem = findBySKU(updateItemRequest.getItemSKU());
        //Only updating new fields
        if (updateItemRequest.getItemName() != null)
            updateItem.setItemName(updateItemRequest.getItemName());
        if (updateItemRequest.getQuantity() != null)
            updateItem.setQuantity(updateItemRequest.getQuantity());
        if (updateItemRequest.getStorageLocation() != null)
            updateItem.setStorageLocation(updateItemRequest.getStorageLocation());
        if (updateItemRequest.getWeight() != null)
            updateItem.setWeight(updateItemRequest.getWeight());
        if (updateItemRequest.getStorageType() != null)
            updateItem.setStorageType(updateItemRequest.getStorageType());
        if (updateItemRequest.getArrivalDate() != null)
            updateItem.setArrivalDate(updateItemRequest.getArrivalDate());

        itemRepository.save(updateItem);
        return new UpdateItemResponse("Successfully updated information about Item: " + updateItem.getItemName());
    }

    /**
     * A method to increase the quantity of an item in the Items table
     * @param itemSKU
     *  The item to increase the quantity of
     * @param quantity
     *  The amount to increase it by
     * @return
     *  A status message containing the Item's new quantity
     */
    public String increaseItemQuantity(Integer itemSKU, int quantity) {
        Item item = findById(itemSKU);
        item.setQuantity(item.getQuantity() + quantity);
        itemRepository.save(item);
        return "New quantity for " + item.getItemName() + ": " + item.getQuantity();
    }

    /**
     * A method to decrease the quantity of an item in the Items table
     * @param itemSKU
     *  The item to decrease the quantity of
     * @param quantity
     *  The amount to decrease it by
     * @return
     *  A status message containing the Item's new quantity
     * @throws IllegalActionException
     *  A custom RuntimeException that handles when a user tries to
     *  lower an Item's quantity below 0
     */
    public String decreaseItemQuantity(Integer itemSKU, int quantity) {
        Item item = findById(itemSKU);
        if (item.getQuantity() <= quantity) {
            throw new IllegalActionException("Cannot lower item quantity below 0");
        }
        else {
            item.setQuantity(item.getQuantity() - quantity);
            itemRepository.save(item);
            return "New quantity for " + item.getItemName() + ": " + item.getQuantity();
        }
    }

    /**
     * A method that deletes an Item from the Items table
     * @param id
     *  The id the item
     */
    public void deleteById(Integer id) {itemRepository.deleteById(id);}

    /**
     * A method that deletes all Items from the database
     */
    public void deleteAll() {itemRepository.deleteAll();}
}
