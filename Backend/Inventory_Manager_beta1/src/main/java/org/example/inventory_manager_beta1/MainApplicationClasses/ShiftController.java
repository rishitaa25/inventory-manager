package org.example.inventory_manager_beta1.MainApplicationClasses;

import org.example.inventory_manager_beta1.DTO.Employees.Shifts.ShiftRequest;
import org.example.inventory_manager_beta1.DTO.Employees.Shifts.ShiftUpdateRequest;
import org.example.inventory_manager_beta1.Entities.Shift;
import org.example.inventory_manager_beta1.Services.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * A controller class containing all the different HTTP mappings for Shift related methods
 */
@RestController
@RequestMapping("/shift")
public class ShiftController {
    /**
     * The auto-injected ShiftService class for handling Shift related logic
     */
    @Autowired
    private ShiftService shiftService;

    /**
     * CREATE: A controller method for handling Shift creation
     * @param newShift
     *  The DTO containing the Shift information in JSON format
     * @return
     *  A message confirming if the Shift creation was successful
     */
    @PostMapping("/create")
    public String createShift(@RequestBody ShiftRequest newShift) {return shiftService.createShift(newShift).getMessage();}

    /**
     * READ: A controller method for handling searching for all Shifts on a given day
     * @param shiftDate
     *  The search date
     * @return
     *  A List of Shifts that are scheduled on the search date
     */
    @GetMapping("/find-by/shift-date/{shiftDate}")
    public List<Shift> getShiftsViaDate(@PathVariable("shiftDate") LocalDate shiftDate) {return shiftService.findByShiftDate(shiftDate);}

    /**
     * READ: A controller method for handling searching for all Shifts with a specific duration
     * @param duration
     *  The duration of the Shift
     * @return
     *  A List of Shifts that have a matching duration
     */
    @GetMapping("/find-by/duration/{duration}")
    public List<Shift> getShiftsViaDuration(@PathVariable("duration") Double duration) {return shiftService.findByDuration(duration);}

    /**
     * READ: A controller method for handling searching for date specific Shifts with a specific duration
     * @param duration
     *  The duration of the Shift
     * @param shiftDate
     *  The assigned date of the Shift
     * @return
     *  A List of Shifts that have a matching assigned date and duration
     */
    @GetMapping("/find-by/duration/{duration}/and-date/{shiftDate}")
    public List<Shift> getShiftsViaDurationAndDate(@PathVariable("duration") Double duration, @PathVariable("shiftDate") LocalDate shiftDate) {return shiftService.findByDurationAndDate(duration, shiftDate);}

    /**
     * READ: A controller method for handling searching for all Shifts with a specific start time
     * @param startTime
     *  The start time of the Shift
     * @return
     *  A List of all Shifts that have a matching start time
     */
    @GetMapping("/find-by/start-time/{startTime}")
    public List<Shift> getShiftsViaStartTime(@PathVariable("startTime") LocalTime startTime) {return shiftService.findByStartTime(startTime);}

    /**
     * READ: A controller method for handling searching for date specific Shifts with a specific start time
     * @param startTime
     *  The start time of the Shift
     * @param shiftDate
     *  The assigned date of the Shift
     * @return
     *  A List of all Shifts that have a matching assigned date and start time
     */
    @GetMapping("/find-by/start-time{startTime}/and-date/{shiftDate}")
    public List<Shift> getShiftViaStartTimeAndDate(@PathVariable("startTime") LocalTime startTime, @PathVariable("shiftDate") LocalDate shiftDate) {return shiftService.findByStartTimeAndDate(startTime, shiftDate);}

    /**
     * READ: A controller method for handling searching for all Shifts with a specific end time
     * @param endTime
     *  The end time of the Shift
     * @return
     *  A List of all Shifts that have a matching end time
     */
    @GetMapping("/find-by/end-time{endTime}")
    public List<Shift> getShiftViaEndTime(@PathVariable("endTime") LocalTime endTime) {return shiftService.findByEndTime(endTime);}

    /**
     * READ: A controller method for handling searching for date specific Shifts with a specific end time
     * @param endTime
     *  The end time of the shift
     * @param shiftDate
     *  The assigned date of the Shift
     * @return
     *  A List of all Shifts that have a matching assigned date and end time
     */
    @GetMapping("/find-by/end-time/{endTime}/and-date/{shiftDate}")
    public List<Shift> getShiftViaEndTimeAndDate(@PathVariable("endTime") LocalTime endTime, @PathVariable("shiftDate") LocalDate shiftDate) {return shiftService.findByEndTimeAndDate(endTime, shiftDate);}

    /**
     * READ: A controller method for handling searching for all Shifts within a date range
     * @param startingDay
     *  The start date of the range
     * @param endingDay
     *  The end date of the range
     * @return
     *  A list of Shifts that fall within the given date range
     */
    @GetMapping("/find-by/range-from/{startingDay}/to/{endingDay}")
    public List<Shift> getShiftsViaRange(@PathVariable("startingDay") LocalDate startingDay, @PathVariable("endingDay") LocalDate endingDay) {return shiftService.findByShiftDateBetween(startingDay, endingDay);}

    /**
     * READ: A controller method for handling searching for all Shifts
     * @return
     *  A list containing all current Shifts
     */
    @GetMapping("/find/all")
    public List<Shift> getAllShifts() {return shiftService.findAll();}

    /**
     * UPDATE: A controller method for handling updating an existing Shift
     * @param shiftUpdateRequest
     *  The DTO containing the updated Shift information in JSON format
     * @return
     *  A message confirming a successful Shift update
     */
    @PutMapping("/update")
    public String updateShift(@RequestBody ShiftUpdateRequest shiftUpdateRequest) {return shiftService.updateShift(shiftUpdateRequest).getMessage();}

    /**
     * TRANSFER: A controller method for handling transferring a Shift to a different Employee
     * @param shiftId
     *  The ID of the Shift to be transferred
     * @param newEmployeeId
     *  The ID of the Employee to transfer the Shift to
     * @return
     *  True if the transfer was completed successfully
     */
    @PatchMapping("/transfer/{shiftId}/to-employee/{newEmployeeId}")
    public Boolean transferShift(@PathVariable("shiftId") Integer shiftId, @PathVariable("newEmployeeId") Integer newEmployeeId) {
        shiftService.transferShift(shiftId, newEmployeeId);
        return true;
    }

    /**
     * DELETE: A controller method for handling specific Shift deletion
     * @param employeeId
     *  The ID of the Employee the shift belongs to
     * @param shiftDay
     *  The date the shift is scheduled
     * @return
     *  True if completed
     */
    @DeleteMapping("/delete/{shiftDay}/from-employee/{employeeId}")
    public Boolean deleteShift(@PathVariable("shiftDay") LocalDate shiftDay, @PathVariable("employeeId") Integer employeeId) {
        shiftService.deleteShift(employeeId, shiftDay);
        return true;
    }

    /**
     * DELETE: A controller method for handling removing all Shifts
     * @return
     *  True if completed
     */
    @DeleteMapping("/delete-all")
    public Boolean deleteAllShifts() {
        shiftService.deleteAllShifts();
        return true;
    }
}