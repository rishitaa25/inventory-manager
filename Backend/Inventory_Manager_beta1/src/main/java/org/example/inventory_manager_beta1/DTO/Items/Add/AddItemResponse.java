package org.example.inventory_manager_beta1.DTO.Items.Add;

import lombok.Data;
import org.example.inventory_manager_beta1.Entities.Item;

/**
 * A lightweight Data Transfer Object used for sending a status message
 * from backend to frontend with information about Item creation
 */
@Data
public class AddItemResponse {
    /**
     * An Item variable used for transferring the new Item
     */
    private Item item;
    /**
     * A String variable used for transferring the Item's name
     */
    private String itemName;
    /**
     * An Integer variable used for transferring the Item's amount
     */
    private Integer amountOfItem;
    /**
     * A String variable used for transferring a confirmation message
     */
    private String message;
    /**
     * INIT: The All Args constructor for creating an AddItemResponse
     * @param item
     *  The new Item created
     * @param itemName
     *  The name of the Item being created
     * @param amountOfItem
     *  The amount of the Item
     */
    public AddItemResponse(Item item, String itemName, Integer amountOfItem) {
        this.item = item;
        this.itemName = itemName;
        this.amountOfItem = amountOfItem;
        message = "Item successfully created. Name: " + itemName + " , Quantity: " + amountOfItem;
    }
}
