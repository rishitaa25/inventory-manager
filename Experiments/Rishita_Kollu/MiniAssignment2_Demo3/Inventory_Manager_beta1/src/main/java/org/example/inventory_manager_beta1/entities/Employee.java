package org.example.inventory_manager_beta1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Employees")
@ToString //Auto-generated toString method
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "ssn", unique = true)
    @ToString.Exclude //Excluded from toString()
    private String ssn;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "access_level")
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Transient //Not part of table for security, can still be accessed and set
    @ToString.Exclude
    private String password;

    public Employee() {}

    public Employee(String ssn, String userName) {
        this.ssn = ssn;
        this.userName = userName;
    }

    //Minimal args constructor
    public Employee(String ssn, String firstName, String userName) {
        this.ssn = ssn;
        this.firstName = firstName;
        this.userName = userName;
    }

    //Full constructor
    public Employee(String ssn, String firstName, String lastName, String userName, String email, String phoneNumber) {
        this.ssn = ssn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
