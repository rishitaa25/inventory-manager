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
@Table(name = "Managers")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ssn")
    @NotFound(action = NotFoundAction.IGNORE)
    private Integer ssn;

    @Column(name = "First Name")
    @NotFound(action = NotFoundAction.IGNORE)
    private String firstName;

    @Column(name = "Last Name")
    @NotFound(action = NotFoundAction.IGNORE)
    private String lastName;

    @Column(name = "username")
    @NotFound(action = NotFoundAction.IGNORE)
    private String username;

    @Column(name = "Access Level")
    @NotFound(action = NotFoundAction.IGNORE)
    private Integer accessLevel;

    @Column(name = "email")
    @NotFound(action = NotFoundAction.IGNORE)
    private String email;

    @Column(name = "phoneNumber")
    @NotFound(action = NotFoundAction.IGNORE)
    private String phoneNumber;

    //Not part of table for security
    private String passwordHash;

    public Admin() {}

    public Admin(Integer ssn, String firstName, String lastName, String username, String passwordHash, Integer accessLevel, String email, String phoneNumber) {
        this.ssn = ssn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.accessLevel = accessLevel;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append(ssn)
                .append(firstName)
                .append(lastName)
                .append(username)
                .append(passwordHash)
                .append(accessLevel)
                .append(email)
                .append(phoneNumber).toString();
    }
}
