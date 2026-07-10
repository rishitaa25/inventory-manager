package org.example.inventory_manager_beta1.repositories;

import org.example.inventory_manager_beta1.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Optional<Inventory> findByItemName(String itemName);
}
