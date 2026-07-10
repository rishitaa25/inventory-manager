package org.example.inventory_manager_beta1.services;

import org.example.inventory_manager_beta1.entities.InventoryChange;
import org.example.inventory_manager_beta1.repositories.InventoryChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryChangeService {

    @Autowired
    private final InventoryChangeRepository inventoryChangeRepository;

    /**
     * INIT:
     * @param inventoryChangeRepository
     */
    public InventoryChangeService(InventoryChangeRepository inventoryChangeRepository) {this.inventoryChangeRepository = inventoryChangeRepository;}

    public void addChange(InventoryChange change){
        inventoryChangeRepository.save(change);
    }

    public List<InventoryChange> findAll(){
        return inventoryChangeRepository.findAll();
    }

    public void deleteChange(Integer id){
        inventoryChangeRepository.deleteById(id);
    }
}
