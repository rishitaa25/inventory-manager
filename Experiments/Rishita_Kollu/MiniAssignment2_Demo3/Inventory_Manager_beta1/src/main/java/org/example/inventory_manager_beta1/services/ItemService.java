package org.example.inventory_manager_beta1.services;

import org.example.inventory_manager_beta1.DataEnums.StorageType;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.ItemNotFoundException;
import org.example.inventory_manager_beta1.entities.Item;
import org.example.inventory_manager_beta1.entities.ShippingCompany;
import org.example.inventory_manager_beta1.repositories.ItemRepository;
import org.example.inventory_manager_beta1.repositories.ShippingCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class ItemService {
    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final ShippingCompanyRepository shippingCompanyRepository;

    /**
     * INIT:
     * @param itemRepository
     */
    public ItemService(ItemRepository itemRepository, ShippingCompanyRepository shippingCompanyRepository) {
        this.itemRepository = itemRepository;
        this.shippingCompanyRepository = shippingCompanyRepository;
    }

    /*======================================================Search function implementation======================================================*/

    /**
     * A search method that finds all items from the Items table
     * @return
     *  A list of all Items currently in the database
     */
    public List<Item> findAll() {return itemRepository.findAll();}

    /**
     * A search method that finds an item by name
     * @param itemName
     *  The name of the item
     * @return
     *  The item with the corresponding name
     */
    public List<Item> findByItemName(String itemName) {return itemRepository.findByItemName(itemName);}

    /**
     * A search method that finds an item by skuId
     * @param skuId
     *  the skuId of the item
     * @return
     *  The item with the corresponding skuId
     */
    public Item findBySKU(Integer skuId) {
        return itemRepository.findById(skuId)
                .orElseThrow(() -> new ItemNotFoundException(STR."Item with SKU: \{skuId} not found"));
    }

    /**
     * A search method that finds all items that are stocked at a certain quantity or higher
     * @param quantity
     *  The minimum quantity an item needs to have
     * @return
     *  A list of all items that have the minimum quantity or higher
     */
    public List<Item> findByQuantity(Integer quantity) {return itemRepository.findByQuantityGreaterThanEqual(quantity);}

    /**
     * A search method that finds all items that are stocked at a certain weight or higher
      * @param weight
     *  The minimum weight, in lbs, an item needs to have
     * @return
     *  A list of all items that have the minimum weight or higher
     */
    public List<Item> findByWeight(Double weight) {return itemRepository.findByWeightGreaterThanEqual(weight);}

    /**
     * A search method that finds all items of a certain storage type
     * @param storageType
     *  The storage type of the item
     * @return
     *  A list of all items with that storage type
     */
    public List<Item> findByStorageType(StorageType storageType) {return itemRepository.findByStorageType(storageType);}

    /*======================================================Logic function implementation======================================================*/

    /**
     * A method to add a completely new item to the Items table
     * @param item
     *  The item you want to add
     */
    public void addNewItem(Item item) {
        if (item.getShippingCompany() == null) {
            List<ShippingCompany> companies = shippingCompanyRepository.findAll();
            if (!companies.isEmpty()) {
                Random random = new Random();
                item.setShippingCompany(companies.get(random.nextInt(companies.size())));
            }
        }
        itemRepository.save(item);
    }

    /**
     * A method to increase the quantity of an item in the Items table
     * @param itemId
     *  The item to increase the quantity of
     * @param quantity
     *  The amount to increase it by
     */
    public void increaseItemQuantity(Integer itemId, int quantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setQuantity(item.getQuantity() + quantity);
        itemRepository.save(item);
    }

    /**
     * A method to decrease the quantity of an item in the Items table
     * @param itemId
     *  The item to decrease the quantity of
     * @param quantity
     *  The amount to decrease it by
     */
    public void decreaseItemQuantity(Integer itemId, int quantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (item.getQuantity() <= quantity) {
            item.setQuantity(0);
        }
        else {
            item.setQuantity(item.getQuantity() - quantity);
        }
        itemRepository.save(item);
    }

    /**
     * A method that deletes an item from the Items table
     * @param id
     *  The id the item
     */
    public void deleteById(Integer id) {itemRepository.deleteById(id);}
}
