package com.nexus.experiment2.people;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Person {
    private String firstName;
    private String lastName;
    private String address;
    private String telephone;

    private String category;

    public Person(String firstName, String lastName,
                  String address, String telephone,
                  String category) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
        this.category = category;
    }

    @Override
    public String toString() {
        return firstName + " " +
                lastName + " " +
                address + " " +
                telephone + " " +
                category;
    }
}

