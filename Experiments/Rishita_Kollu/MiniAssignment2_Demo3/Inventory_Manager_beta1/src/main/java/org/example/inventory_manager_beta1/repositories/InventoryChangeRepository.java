package org.example.inventory_manager_beta1.repositories;

import org.example.inventory_manager_beta1.entities.InventoryChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryChangeRepository extends JpaRepository<InventoryChange, Integer> {

}
