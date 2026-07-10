package org.example.inventory_manager_beta1.DTO.Employees.Shifts;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A lightweight Data Transfer Object used for sending an update request
 * from frontend to backend with updated Shift information for an existing Shift.
 * NotNull constrained variables must be present in order for the update
 * to be completed.
 */
@Data
public class ShiftUpdateRequest {
    /**
     * A NotNull constrained Integer variable for transferring
     * the ID of the Employee working the Shift
     */
    @NotNull
    private Integer employeeId;
    /**
     * A NotNull constrained LocalDate variable for transferring
     * the Shift's date
     */
    @NotNull
    private LocalDate shiftDate;
    /**
     * A LocalTime variable for transferring the Shift's start time
     */
    private LocalTime startTime;
    /**
     * A LocalTime variable for transferring the Shift's end time
     */
    private LocalTime endTime;
}
