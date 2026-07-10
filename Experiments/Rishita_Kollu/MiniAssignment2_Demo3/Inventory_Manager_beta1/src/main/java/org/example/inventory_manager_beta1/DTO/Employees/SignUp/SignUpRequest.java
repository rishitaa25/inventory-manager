package org.example.inventory_manager_beta1.DTO.Employees.SignUp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
//Class to handle transferring data from JSON file
public class SignUpRequest {
    @NotBlank //Only required field(s) to make an employee on signup
    private String ssn;
    @NotBlank
    private String firstName;
    private String lastName;
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    @Email
    private String email;
    private String phoneNumber;
}
