package org.example.inventory_manager_beta1.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.inventory_manager_beta1.DataEnums.ManagementTitle;

/**
 * A robust Entity class for modeling an Admin within the inventory management system
 */
@Entity
@Getter
@Setter
@Table(name = "Admins")
@ToString(callSuper = true)
public class Admin extends Employee{
    /**
     * A ManagementTitle Enumerated Constant for representing the Amin's management title.
     * The column is separate from the Employee table within H2 or MySQL
     */
    @Column(name = "management_title")
    @Enumerated(EnumType.STRING)
    private ManagementTitle managementTitle;

    /**
     * INIT: The No Args constructor required by SpringBoot
     */
    public Admin() {}

    /**
     * INIT: The Minimal Args constructor for creating an Admin object
     * @param ssn
     *  The Social Security Number of the Admin
     * @param firstName
     *  The first name of the Admin
     * @param userName
     *  The username of the Admin
     */
    public Admin(String ssn, String firstName, String userName) {super(ssn, firstName, userName);}

    /**
     * INIT: The All Args constructor for creating an Admin object
     * @param ssn
     *  The Social Security Number of the Admin
     * @param firstName
     *  The first name of the Admin
     * @param lastName
     *  The last name of the Admin
     * @param userName
     *  The username of the Admin
     * @param email
     *  The email address of the Admin
     * @param phoneNumber
     *  The phone number of the Admin
     * @param managementTitle
     *  The management title of the Admin
     */
    public Admin(String ssn, String firstName, String lastName, String userName, String email, String phoneNumber, ManagementTitle managementTitle) {
        super(ssn, firstName, lastName, userName, email, phoneNumber);
        this.managementTitle = managementTitle;
    }
}
