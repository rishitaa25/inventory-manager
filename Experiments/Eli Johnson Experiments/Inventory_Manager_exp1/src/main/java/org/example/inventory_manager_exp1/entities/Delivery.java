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
@Table(name = "Deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Driver ID")
    @NotFound(action = NotFoundAction.IGNORE)
    private String driverID;

    @Column(name = "Driver Name")
    @NotFound(action = NotFoundAction.IGNORE)
    private String driverName;

    @Column(name = "Access Level")
    @NotFound(action = NotFoundAction.IGNORE)
    private Integer accessLevel;

    @Column(name = "Company")
    @NotFound(action = NotFoundAction.IGNORE)
    private String company;

    @Column(name = "Delivery Day")
    @NotFound(action = NotFoundAction.IGNORE)
    private String deliveryDay;

    public Delivery() {}

    public Delivery(String driverID, String driverName, Integer accessLevel, String company, String deliveryDay) {
        this.driverID = driverID;
        this.driverName = driverName;
        this.accessLevel = accessLevel;
        this.company = company;
        this.deliveryDay = deliveryDay;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append(driverID)
                .append(driverName)
                .append(accessLevel)
                .append(company)
                .append(deliveryDay)
                .toString();
    }
}
