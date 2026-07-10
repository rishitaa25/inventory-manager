package org.example.inventory_manager_beta1.Repositories;

import jakarta.annotation.Nonnull;
import org.example.inventory_manager_beta1.Entities.InventoryChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * A repository containing all the custom database searches relevant to the InventoryChange
 * class. All Optional wrapped search methods use the custom Employee Exception handler for
 * better HTTP Exception handling
 */
@Repository
public interface InventoryChangeRepository extends JpaRepository<InventoryChange, Integer> {
    /**
     * A JpaRepository search method (overridden to use custom Exception handling) to
     * search a database for an InventoryChange using their identification number
     * (assigned at InventoryChange creation)
     * @param id
     *  The identification number of the InventoryChange being searched for
     * @return
     *  The InventoryChange with the matching ID if it exists
     */
    @Override
    @Nonnull Optional<InventoryChange> findById(@Nonnull Integer id);
}
