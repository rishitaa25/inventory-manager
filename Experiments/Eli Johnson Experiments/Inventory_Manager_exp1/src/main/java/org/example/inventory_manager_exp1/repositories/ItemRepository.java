package org.example.inventory_manager_exp1.repositories;

import org.example.inventory_manager_exp1.entities.Employee;
import org.example.inventory_manager_exp1.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Employee, Integer> {
    Optional<Item> findByItemName (String itemName);
    Optional<Item> findByQuantity (Integer quantity);
    Optional<Item> findByWeight (Integer weight);
    Optional<Item> findByRefrigerated (Boolean refrigerated);
}
