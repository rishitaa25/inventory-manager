package com.nexus.experiment3.people;

import lombok.Getter; // lombok library automatically generates getters and setters
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Email; // pre built library for validation
import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class Person {

    @NotEmpty(message = "First name cannot be empty") // first name cannot be empty - validation error
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    private String address;

    private String telephone;

    @Email(message = "Invalid email format") // validates email format
    private String email;

    private String category;// to be defined in the future

    private boolean active = true; // tracks if person is active or not (as of now automatically true)

    private int importance;  // integer to rank importance (lower number, higher importance)

    public Person(String firstName, String lastName, String address,
                  String telephone, String email,
                  String category, boolean active, int importance) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.category = category;
        this.active = active;
        this.importance = importance;
    }
}
