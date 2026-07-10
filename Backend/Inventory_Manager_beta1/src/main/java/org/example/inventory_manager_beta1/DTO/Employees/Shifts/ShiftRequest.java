package org.example.inventory_manager_beta1.DTO.Employees.Shifts;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A lightweight Data Transfer Object used for sending shift information
 * from frontend to backend, which is used to create a shift for an Employee.
 * NotNull constrained variables must be present in order
 * for the shift request to be processed.
 */
@Data
public class ShiftRequest {
    /**
     * A NotNull constrained Integer variable for transferring the
     * Employee's ID
     */
    @NotNull
    private Integer employeeId;
    /**
     * A NotNull constrained LocalDate variable for transferring the
     * Shift's date
     */
    @NotNull
    private LocalDate shiftDate;
    /**
     * A NotNull constrained LocalTime variable for transferring the
     * Shift's start time
     */
    @NotNull
    private LocalTime startTime;
    /**
     * A NotNull constrained LocalTime variable for transferring the
     * Shift's end time
     */
    @NotNull
    private LocalTime endTime;
}
