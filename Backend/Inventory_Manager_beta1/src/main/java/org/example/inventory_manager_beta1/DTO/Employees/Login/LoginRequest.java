package org.example.inventory_manager_beta1.DTO.Employees.Login;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A lightweight Data Transfer Object used for sending login information
 * from frontend to backend, which is used to verify an Employee's login.
 * NotBlank constrained variables must be present in order for the login request
 * to be processed.
 */
@Data
@AllArgsConstructor
public class LoginRequest {
    /**
     * A NotBlank constrained String variable used for transferring
     * the Employee's username
     */
    @NotBlank
    private String userName;
    /**
     * A NotBlank constrained String variable used for transferring
     * the Employee's password
     */
    @NotBlank
    private String password;
}
