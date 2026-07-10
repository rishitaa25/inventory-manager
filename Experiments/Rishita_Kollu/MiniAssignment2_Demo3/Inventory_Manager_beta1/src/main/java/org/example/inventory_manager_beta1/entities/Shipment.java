package org.example.inventory_manager_beta1.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Shipments")
@ToString(exclude = {"items", "drivers", "shippingCompanies"})
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Integer shipmentId;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "arrived")
    private Boolean arrived;

    @Column(name = "offloaded")
    private Boolean offloaded;

    @OneToMany(mappedBy = "shipment", cascade = {CascadeType.ALL})
    private List<Item> items;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "shipment_companies",
            joinColumns = @JoinColumn(name = "shipment_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    @JsonIgnoreProperties("shipments")
    private List<ShippingCompany> shippingCompanies;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "shipment_drivers",
            joinColumns = @JoinColumn(name = "shipment_id"),
            inverseJoinColumns = @JoinColumn(name = "driver_id")
    )
    @JsonIgnoreProperties("shipments")
    private List<Driver> drivers;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnoreProperties("shipments")
    private Admin creationAdmin;

    public Shipment() {}

    public Shipment(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
        this.arrived = false;
        this.offloaded = false;
    }

    public Shipment(LocalDate deliveryDate, Admin creationAdmin, List<Item> items) {
        this.deliveryDate = deliveryDate;
        this.creationAdmin = creationAdmin;
        this.items = items;
        this.arrived = false;
        offloaded = false;
        this.drivers = new ArrayList<>();
        this.shippingCompanies = new ArrayList<>();
    }
}
