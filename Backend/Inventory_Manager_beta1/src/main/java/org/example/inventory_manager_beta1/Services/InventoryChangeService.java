package org.example.inventory_manager_beta1.Services;

import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.InventoryChangeNotFoundException;
import org.example.inventory_manager_beta1.Entities.InventoryChange;
import org.example.inventory_manager_beta1.Repositories.InventoryChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * A robust service class to handle all the different logic and search methods relating
 * to the InventoryChange class. It is complete with all the InventoryChangeRepository
 * search methods defined, and it handles every InventoryChange related logic method
 * with included custom error handling
 */
@Service
public class InventoryChangeService {
    /**
     * The auto-injected InventoryChangeRepository for handling custom searches
     */
    @Autowired
    private final InventoryChangeRepository inventoryChangeRepository;

    /**
     * INIT: Used for initializing the inventoryChangeRepository for custom database searches
     * @param inventoryChangeRepository
     *  The repository containing the custom search methods
     */
    public InventoryChangeService(InventoryChangeRepository inventoryChangeRepository) {this.inventoryChangeRepository = inventoryChangeRepository;}

    /*==================================SEARCH METHODS==================================*/

    /**
     * SEARCH: A search method for finding an InventoryChange using its
     * identification number
     * @param id
     *  The identification number of the InventoryChange being searched for
     * @return
     *  The InventoryChange with the matching ID number if it exists
     * @throws InventoryChangeNotFoundException
     *  Custom NotFound exception if the InventoryChange ID search returns null
     */
    public InventoryChange findById(Integer id) {
        return inventoryChangeRepository.findById(id)
                .orElseThrow(() -> new InventoryChangeNotFoundException("Inventory change instance with ID: " + id + " not found"));
    }

    /*==================================LOGIC METHODS==================================*/

    /**
     * CREATE: A logic method that saves a new InventoryChange to the database
     * @param change
     *  The InventoryChange being saved
     */
    public void addChange(InventoryChange change){inventoryChangeRepository.save(change);}

    /**
     * SEARCH: A search method for finding all InventoryChanges in the database
     * @return
     *  A list of all existing InventoryChanges in the database
     */
    public List<InventoryChange> findAll() {return inventoryChangeRepository.findAll();}

    /**
     * DELETE: A logic method that deletes an InventoryChange from the database
     * using its identification number
     * @param id
     *  The identification number of the InventoryChange being deleted
     */
    public void deleteChange(Integer id){
        inventoryChangeRepository.deleteById(id);
    }
}
