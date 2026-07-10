package org.example.inventory_manager_beta1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "Inventory")
@ToString
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sku_number")
    private Integer skuNumber;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "amount_of_item")
    private Integer amountOfItem;

    public Inventory() {}

    public Inventory(String itemName, Integer amountOfItem) {
        this.itemName = itemName;
        this.amountOfItem = amountOfItem;
    }

    public Inventory(String itemName, String itemDescription, Integer amountOfItem) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.amountOfItem = amountOfItem;
    }
}
