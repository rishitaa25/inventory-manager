package org.example.inventory_manager_beta1.DTO.Items.Update;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A lightweight Data Transfer Object used for sending a status message from
 * backend to frontend with an update confirmation message
 */
@Data
@AllArgsConstructor
public class UpdateItemResponse {
    /**
     * A String variable for transferring a message
     */
    private String message;
}
