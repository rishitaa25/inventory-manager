package org.example.inventory_manager_beta1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "InventoryChanges")
@ToString
public class InventoryChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "change_id")
    private Integer changeId;


    @Column(name = "sku_number")
    private Integer skuNumber;


    @Column(name = "amount_of_item")
    private Integer amountOfItem;

    @Column(name = "employee_id")
    private Integer employeeId;

    public InventoryChange(){}

    public InventoryChange(Integer skuNumber, Integer amountOfItem, Integer employeeId){
        this.skuNumber = skuNumber;
        this.amountOfItem = amountOfItem;
        this.employeeId = employeeId;
    }
}
