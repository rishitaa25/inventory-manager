package org.example.inventory_manager_beta1.DTO.Objects;

import lombok.Data;

/**
 * A lightweight Data Transfer Object used for sending a shortened Item object
 * from backend to frontend.
 */
@Data
public class ShortItem {
    /**
     * An Integer variable for containing/transferring the Item's SKU number
     */
    private Integer skuId;
    /**
     * A String variable for containing/transferring the Item's name
     */
    private String itemName;
    /**
     * A String variable for containing/transferring the Item's description
     */
    private String description;
    /**
     * A String variable for containing/transferring the Item's quantity
     */
    private Integer quantity;
    /**
     * A String variable for containing/transferring the Item's shipping company
     */
    private String shippingCompanyName;

    /**
     * INIT: The All Args constructor for creating a ShortItem
     * @param skuId
     *  The SKU number of the Item
     * @param itemName
     *  The name of the Item
     * @param description
     *  A description of the Item
     * @param quantity
     *  The quantity of the item
     * @param shippingCompanyName
     *  The shipping company that makes the item
     */
    public ShortItem(Integer skuId, String itemName, String description, Integer quantity, String shippingCompanyName) {
        this.skuId = skuId;
        this.itemName = itemName;
        this.description = description;
        this.quantity = quantity;
        this.shippingCompanyName = shippingCompanyName;
    }
}
