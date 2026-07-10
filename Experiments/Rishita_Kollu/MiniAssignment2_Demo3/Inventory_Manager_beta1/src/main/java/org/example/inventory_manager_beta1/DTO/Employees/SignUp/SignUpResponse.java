package org.example.inventory_manager_beta1.DTO.Employees.SignUp;

import lombok.Data;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;

/**
 * A lightweight data transfer object class sent to an employee after a successful signup
 */
@Data
public class SignUpResponse {
    private String message;
    //Variables here so that if front end wants to do anything with them they can
    private Integer employeeId;
    private String firstName;
    private AccessLevel accessLevel;

    public SignUpResponse(Integer employeeId, String firstName, AccessLevel accessLevel) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.accessLevel = accessLevel;
        message = "Thank you for signing up " + firstName + ". Your employee ID is: " + employeeId +  ", and you have an access level of " + accessLevel;
    }
}
