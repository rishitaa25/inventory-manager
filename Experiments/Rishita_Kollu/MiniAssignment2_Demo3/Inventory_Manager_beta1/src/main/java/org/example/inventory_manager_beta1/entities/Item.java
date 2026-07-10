package org.example.inventory_manager_beta1.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.inventory_manager_beta1.DataEnums.StorageType;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "Items")
@ToString
public class Item {
    @Id //Unique item identifier
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sku_id")
    private Integer skuId;

    @Column(name = "item_name")
    private String itemName;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties("items")
    private ShippingCompany shippingCompany;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    @JsonIgnoreProperties("items")
    private Shipment shipment;

    @Column(name = "storage_location")
    private String storageLocation;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "weight_lbs")
    private Double weight;

    @Column(name = "storage_type")
    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    public Item() {
    }

    public Item(String itemName, ShippingCompany shippingCompany, String storageLocation, Integer quantity, Double weight, StorageType storageType, LocalDate arrivalDate) {
        this.itemName = itemName;
        this.shippingCompany = shippingCompany;
        this.storageLocation = storageLocation;
        this.quantity = quantity;
        this.weight = weight;
        this.storageType = storageType;
        this.arrivalDate = arrivalDate;
    }
}
