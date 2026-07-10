package org.example.inventory_manager_beta1.repositories;

import org.example.inventory_manager_beta1.DataEnums.StorageType;
import org.example.inventory_manager_beta1.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByItemName (String itemName);
    List<Item> findByQuantityGreaterThanEqual (Integer quantity);
    List<Item> findByWeightBetween (Double min, Double max);
    List<Item> findByWeightGreaterThanEqual (Double min);
    List<Item> findByStorageType (StorageType storageType);
}
