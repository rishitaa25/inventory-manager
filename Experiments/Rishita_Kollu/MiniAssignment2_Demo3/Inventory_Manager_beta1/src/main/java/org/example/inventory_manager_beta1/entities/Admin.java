package org.example.inventory_manager_beta1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.inventory_manager_beta1.DataEnums.ManagementTitle;

@Entity
@Getter
@Setter
@Table(name = "Admins")
@ToString(callSuper = true)
public class Admin extends Employee{

    //All other columns inherited from Employee
    @Column(name = "management_title")
    @Enumerated(EnumType.STRING)
    private ManagementTitle managementTitle;

    public Admin() {}

    public Admin(String ssn, String firstName, String lastName, String username, String email, String phoneNumber) {
        super(ssn, firstName, lastName, username, email, phoneNumber);
    }
}
