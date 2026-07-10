package org.example.inventory_manager_beta1.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

/**
 * A robust Entity class for modeling a Driver within the inventory management system
 */
@Entity
@Getter
@Setter
@Table(name = "Drivers")
@ToString(callSuper = true)
public class Driver extends Employee{
    /**
     * A List variable representing a list of shipments the Driver has been assigned to.
     * The column is separate from the Employee table in H2 or MySQL
     */
    @ManyToMany(mappedBy = "drivers", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("drivers")
    private List<Shipment> shipments;

    /**
     * INIT: The No Args constructor required by SpringBoot
     */
    public Driver() {}

    /**
     * INIT: The Minimal Args constructor for creating a Driver object
     * @param ssn
     *  The Social Security Number of the Driver
     * @param firstName
     *  The first name of the Driver
     * @param userName
     *  The username of the Driver
     */
    public Driver(String ssn, String firstName, String userName) {
        super(ssn, firstName, userName);
    }

    /**
     * INIT: The All Args constructor for creating a Driver object
     * @param ssn
     *  The Social Security Number of the Driver
     * @param firstName
     *  The first name of the Driver
     * @param lastName
     *  The last name of the Driver
     * @param userName
     *  The username of the Driver
     * @param email
     *  The email address of the Driver
     * @param phoneNumber
     *  The phone number of the Driver
     */
    public Driver(String ssn, String firstName, String lastName, String userName, String email, String phoneNumber) {super(ssn, firstName, lastName, userName, email, phoneNumber);}
}
