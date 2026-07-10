package org.example.inventory_manager_exp1.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.core.style.ToStringCreator;

@Entity
@Getter
@Setter
@Table(name = "Items")
public class Item {
    @Id //Unique item identifier
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "squID")
    @NotFound(action = NotFoundAction.IGNORE) //What to do if item doesn't exist
    private String squID;

    @Column(name = "itemName")
    @NotFound(action = NotFoundAction.IGNORE)
    private String itemName;

    @Column(name = "Location")
    @NotFound(action = NotFoundAction.IGNORE)
    private String location;

    @Column(name = "Quantity")
    @NotFound(action = NotFoundAction.IGNORE)
    private Integer quantity;

    @Column(name = "Weight_(lbs)")
    @NotFound(action = NotFoundAction.IGNORE)
    private Integer weight;

    @Column(name = "refrigerated")
    @NotFound(action = NotFoundAction.IGNORE)
    private Boolean refrigerated;

    public Item() {}

    public Item(String squID, String itemName, String location, Integer quantity, Integer weight, Boolean refrigerated) {
        this.squID = squID;
        this.itemName = itemName;
        this.location = location;
        this.quantity = quantity;
        this.weight = weight;
        this.refrigerated = refrigerated;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append(squID)
                .append(itemName)
                .append(location)
                .append(quantity)
                .append(weight)
                .append(refrigerated).toString();
        }
    }
