package org.example.inventory_manager_beta1.DTO.Employees.Shifts;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A lightweight Data Transfer Object used for sending a status message
 * from backend to frontend with information about the Shift
 */
@Data
@AllArgsConstructor
public class ShiftResponse {
    /**
     * A string variable used for transferring a login status message
     */
    private String message;
}
