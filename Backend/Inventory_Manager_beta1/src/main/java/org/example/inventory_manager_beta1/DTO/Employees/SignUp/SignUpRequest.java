package org.example.inventory_manager_beta1.DTO.Employees.SignUp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.inventory_manager_beta1.DataEnums.ManagementTitle;

/**
 * A lightweight Data Transfer Object used for sending a sign-up request
 * from frontend to backend with information about a new Employee being
 * added to the database. NotBlank constrained variables must be
 * present in order for the Employee to be created
 */
@Data
public class SignUpRequest {
    /**
     * A NotBlank constrained String variable for transferring
     * the Employee's Social Security Number
     */
    @NotBlank
    private String ssn;
    /**
     * A NotBlank constrained String variable for transferring
     * the Employee's first name
     */
    @NotBlank
    private String firstName;
    /**
     * A String variable for transferring the Employee's last name
     */
    private String lastName;
    /**
     * A NotBlank constrained String variable for transferring
     * the Employee's username
     */
    @NotBlank
    private String userName;
    /**
     * A NotBlank constrained String variable for transferring
     * the Employee's password
     */
    @NotBlank
    private String password;
    /**
     * An Email constrained String variable for transferring the
     * Employee's email
     */
    @Email
    private String email;
    /**
     * A String variable for transferring the Employee's phone number
     */
    private String phoneNumber;
    /**
     * An Admin-specific ManagementTitle Enumerated Constant variable
     * for transferring the Admin's management title
     */
    private ManagementTitle managementTitle;
}
