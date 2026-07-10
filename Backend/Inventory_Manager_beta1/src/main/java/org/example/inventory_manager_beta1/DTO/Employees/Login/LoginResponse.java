package org.example.inventory_manager_beta1.DTO.Employees.Login;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A lightweight Data Transfer Object used for sending a status message
 * from backend to frontend with information about the login attempt
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    /**
     * A string variable used for transferring a login status message
     */
    private String message;
    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String accessLevel;
    private String token;

}
