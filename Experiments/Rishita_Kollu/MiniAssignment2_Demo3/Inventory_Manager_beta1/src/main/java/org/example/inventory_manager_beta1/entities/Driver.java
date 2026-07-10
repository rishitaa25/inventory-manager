package org.example.inventory_manager_beta1.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Drivers")
@ToString(callSuper = true)
public class Driver extends Employee{

    //All other columns inherited from Employee
    @ManyToMany(mappedBy = "drivers", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("drivers")
    private List<Shipment> shipments;

    public Driver() {}

    public Driver(String ssn, String firstName, String userName) {
        super(ssn, firstName, userName);
    }

    public Driver(String ssn, String firstName, String lastName, String username, String email, String phoneNumber) {
        super(ssn, firstName, lastName, username, email, phoneNumber);
    }
}
