package org.example.inventory_manager_beta1.Repositories;

import jakarta.annotation.Nonnull;
import org.example.inventory_manager_beta1.Entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * A repository containing all the custom database searches relevant to the Inventory class.
 * All Optional wrapped search methods use the custom Exception handler(s) for better
 * HTTP Exception handling
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    /**
     * A JpaRepository search method (overridden to use custom Exception handling) to
     * search a database for an Inventory using their identification number
     * (assigned at Inventory creation)
     * @param id
     *  The identification number of the Inventory being searched for
     * @return
     *  The Inventory with the matching ID if it exists
     */
    @Override
    @Nonnull Optional<Inventory> findById(@Nonnull Integer id);

    /**
     * A custom JpaRepository search method to search a database for an Inventory entry
     * using its item name
     * @param itemName
     *  The item name of the Inventory entry being searched for
     * @return
     *  The Inventory entry with the matching item name if it exists
     */
    Optional<Inventory> findByItemName(String itemName);
}
