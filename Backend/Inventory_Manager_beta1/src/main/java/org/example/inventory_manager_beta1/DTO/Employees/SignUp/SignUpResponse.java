package org.example.inventory_manager_beta1.DTO.Employees.SignUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;

/**
 * A lightweight Data Transfer Object used for returning a JSON
 * from backend to frontend with Employee sing-up information
 */
@Data
@AllArgsConstructor
public class SignUpResponse {
    /**
     * An Integer variable for containing/transferring the Employee's ID
     */
    private Integer employeeId;
    /**
     * A String variable for containing/transferring the Employee's first name
     */
    private String firstName;
    /**
     * A String variable for containing/transferring the Employee's username
     */
    private String username;
    /**
     * An AccessLevel Enumerated Constant for containing/transferring
     * the Employee's system access level
     */
    private AccessLevel accessLevel;
}
