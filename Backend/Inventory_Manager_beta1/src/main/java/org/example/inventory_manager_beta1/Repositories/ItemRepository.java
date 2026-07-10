package org.example.inventory_manager_beta1.Repositories;

import jakarta.annotation.Nonnull;
import org.example.inventory_manager_beta1.DataEnums.StorageType;
import org.example.inventory_manager_beta1.Entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * A repository containing all the custom database searches relevant to the Item class.
 * All Optional wrapped search methods use the custom Item Exception handler for better
 * HTTP Exception handling
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    /**
     * A JpaRepository search method (overridden to use custom Exception handling) to
     * search a database for an Item using their identification number
     * (assigned at Item creation)
     * @param id
     *  The identification number of the Item being searched for
     * @return
     *  The Item with the matching ID if it exists
     */
    @Override
    @Nonnull Optional<Item> findById(@Nonnull Integer id);

    /**
     * A custom JpaRepository search method to search a database for Item(s)
     * using their item name
     * @param itemName
     *  The item name of the Item(s) being searched for
     * @return
     *  A list of Item(s) with a matching item name
     */
    List<Item> findByItemName(String itemName);

    /**
     * A custom JpaRepository search method to search a database for Item(s)
     * with a quantity greater than or equal to the given value
     * @param quantity
     *  The minimum quantity threshold of the Item(s) being searched for
     * @return
     *  A list of Item(s) with a quantity greater than or equal to the given value
     */
    List<Item> findByQuantityGreaterThanEqual(Integer quantity);

    /**
     * A custom JpaRepository search method to search a database for Item(s)
     * with a weight within the given range
     * @param min
     *  The minimum weight of the range being searched for
     * @param max
     *  The maximum weight of the range being searched for
     * @return
     *  A list of Item(s) whose weight falls within the given range
     */
    List<Item> findByWeightBetween(Double min, Double max);

    /**
     * A custom JpaRepository search method to search a database for Item(s)
     * with a weight greater than or equal to the given value
     * @param min
     *  The minimum weight threshold of the Item(s) being searched for
     * @return
     *  A list of Item(s) with a weight greater than or equal to the given value
     */
    List<Item> findByWeightGreaterThanEqual(Double min);

    /**
     * A custom JpaRepository search method to search a database for Item(s)
     * using their storage type
     * @param storageType
     *  The storage type of the Item(s) being searched for
     * @return
     *  A list of Item(s) with a matching storage type
     */
    List<Item> findByStorageType(StorageType storageType);
}
