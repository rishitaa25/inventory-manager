package org.example.inventory_manager_beta1.DTO.Employees.Update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRequest {
    @NotNull //Expected values for an account update
    private Integer employeeId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    @NotBlank
    private String phoneNumber;
    @Email
    private String email;
}
