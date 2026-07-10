package org.example.inventory_manager_beta1.DTO.Employees.Update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * A lightweight Data Transfer Object used for sending an update request
 * from frontend to backend with updated user information for an existing Employee.
 * NotNull constrained variables must be present in order for the update
 * to be completed.
 */
@Data
public class UpdateEmployeeRequest {
    /**
     * A NotNull constrained Integer variable for transferring
     * the Employee's identification number
     */
    @NotNull
    private Integer employeeId;
    /**
     * A String variable for transferring
     * the Employee's updated first name
     */
    private String firstName;
    /**
     * A String variable for transferring
     * the Employee's updated last name
     */
    private String lastName;
    /**
     * A String variable for transferring
     * the Employee's updated username
     */
    private String userName;
    /**
     * A String variable for transferring
     * the Employee's updated unhashed password
     */
    private String password;
    /**
     * A String variable for transferring
     * the Employee's updated phone number
     */
    private String phoneNumber;
    /**
     * An Email constrained String variable for transferring
     * the Employee's updated email
     */
    @Email
    private String email;
}
