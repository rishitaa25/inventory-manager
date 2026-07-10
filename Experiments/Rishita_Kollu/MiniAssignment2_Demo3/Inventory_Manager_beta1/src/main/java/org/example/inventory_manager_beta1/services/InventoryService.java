package org.example.inventory_manager_beta1.services;

import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.InventoryNotFoundException;
import org.example.inventory_manager_beta1.entities.Inventory;
import org.example.inventory_manager_beta1.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private final InventoryRepository inventoryRepository;

    /**
     * INIT:
     * @param inventoryRepository
     *
     */
    public InventoryService(InventoryRepository inventoryRepository) {this.inventoryRepository = inventoryRepository;}

    public void saveItem(Inventory inventory){
        inventoryRepository.save(inventory);
    }

    public List<Inventory> findAll(){
        return inventoryRepository.findAll();
    }

    /**
     *
     * @param sku
     * @return
     *
     */
    public Inventory findBySku(Integer sku) {
        return inventoryRepository.findById(sku)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory item not found"));
    }

    public Optional<Inventory> findByItemName(String itemName) {
        return inventoryRepository.findByItemName(itemName);
    }

    public void deleteItem(Integer sku){
        inventoryRepository.deleteById(sku);
    }
}
