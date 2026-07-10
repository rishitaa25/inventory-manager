package org.example.inventory_manager_beta1.DTO.Employees.Update;

import lombok.Data;

/**
 * A lightweight Data Transfer Object used for sending a status message from
 * backend to frontend with an update confirmation message
 */
@Data
public class UpdateEmployeeResponse {
    /**
     * A String variable for transferring a message
     */
    private String message;
    /**
     * An Integer variable for containing/transferring the Employee's ID
     */
    private Integer employeeId;
    /**
     * A String variable for containing/transferring the Employee's username
     */
    private String userName;

    /**
     * INIT: The All Args constructor for creating an UpdateResponse
     * @param employeeId
     *  The ID of the Employee who was just updated
     * @param userName
     *  The username of the Employee who was just updated
     */
    public UpdateEmployeeResponse(Integer employeeId, String userName) {
        this.employeeId = employeeId;
        message = "Successfully updated employee information for: " + userName + " (ID: " + employeeId + ")";
    }
}
