package org.example.inventory_manager_beta1.Services;

import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.InventoryNotFoundException;
import org.example.inventory_manager_beta1.Entities.Inventory;
import org.example.inventory_manager_beta1.Repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * A robust service class to handle all the different logic and search methods relating
 * to the Inventory class. It is complete with all the InventoryRepository search methods
 * defined, and it handles every Inventory related logic method with included custom error handling
 */
@Service
public class InventoryService {
    /**
     * The auto-injected InventoryRepository for handling custom searches
     */
    @Autowired
    private final InventoryRepository inventoryRepository;

    /**
     * INIT: Used for initializing the inventoryRepository for custom database searches
     * @param inventoryRepository
     *  The repository containing the custom search methods
     */
    public InventoryService(InventoryRepository inventoryRepository) {this.inventoryRepository = inventoryRepository;}

    /*==================================SEARCH METHODS==================================*/

    /**
     * SEARCH: A search method for finding an Inventory using its
     * identification number
     * @param id
     *  The identification number of the Inventory being searched for
     * @return
     *  The Inventory with the matching ID number if it exists
     * @throws InventoryNotFoundException
     *  Custom NotFound exception if the Inventory ID search returns null
     */
    public Inventory findById(Integer id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory with ID: " + id + " not found"));
    }

    /**
     * SEARCH: A search method for finding all Inventory entries in the database
     * @return
     *  A list of all existing Inventory entries in the database
     */
    public List<Inventory> findAll() {return inventoryRepository.findAll();}

    /**
     * SEARCH: A search method for finding an Inventory entry using its SKU number
     * @param sku
     *  The SKU number of the Inventory entry being searched for
     * @return
     *  The Inventory entry with the matching SKU number if it exists
     * @throws InventoryNotFoundException
     *  Custom NotFound exception if the Inventory SKU search returns null
     */
    public Inventory findBySku(Integer sku) {
        return inventoryRepository.findById(sku)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory item with SKU: " + sku + " not found"));
    }

    /**
     * SEARCH: A search method for finding an Inventory entry using its name
     * @param itemName
     *  The name of the Inventory entry being searched for
     * @return
     *  The Inventory entry with the matching name if it exists
     * @throws InventoryNotFoundException
     *  Custom NotFound exception if the Inventory name search returns null
     */
    public Inventory findByItemName(String itemName) {
        return inventoryRepository.findByItemName(itemName)
                .orElse(null);
    }

    /*==================================LOGIC METHODS==================================*/

    /**
     * UPDATE: A logic method that saves an Inventory entry to the database
     * @param inventory
     *  The Inventory entry being saved
     */
    public void saveItem(Inventory inventory) {inventoryRepository.save(inventory);}

    /**
     * DELETE: A logic method that deletes an Inventory entry from the database
     * using its SKU number
     * @param sku
     *  The SKU number of the Inventory entry being deleted
     */
    public void deleteItem(Integer sku){
        inventoryRepository.deleteById(sku);
    }
}
