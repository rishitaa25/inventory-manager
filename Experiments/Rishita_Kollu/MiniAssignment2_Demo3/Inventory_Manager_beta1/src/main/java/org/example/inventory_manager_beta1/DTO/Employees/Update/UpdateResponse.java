package org.example.inventory_manager_beta1.DTO.Employees.Update;

import lombok.Data;

@Data
public class UpdateResponse {
    private String message;
    private Integer employeeId;
    private String userName;

    public  UpdateResponse(Integer employeeId, String userName) {
        this.employeeId = employeeId;
        message = STR."Successfully updated employee details for: \{userName}";
    }
}
