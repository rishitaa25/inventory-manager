package org.example.inventory_manager_beta1.Services;

import org.example.inventory_manager_beta1.DTO.Employees.Shifts.*;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.EmployeeNotFoundException;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.IllegalActionException;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.ShiftNotFoundException;
import org.example.inventory_manager_beta1.Entities.Employee;
import org.example.inventory_manager_beta1.Entities.Shift;
import org.example.inventory_manager_beta1.Repositories.EmployeeRepository;
import org.example.inventory_manager_beta1.Repositories.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * A robust service class to handle all the different logic and search methods relating
 * to the Shift class. It is complete with all the ShiftRepository search methods
 * defined, and it handles every Shift related logic method with included custom error handling
 */
@Service
public class ShiftService {
    /**
     * The auto-injected ShiftRepository for handling custom Shift searches
     */
    @Autowired
    private final ShiftRepository shiftRepository;

    /**
     * The auto-injected EmployeeRepository for handling custom Employee searches
     */
    @Autowired
    private final EmployeeRepository employeeRepository;

    /**
     * INIT: Used for initializing the repositories for custom database checks and searches
     * @param shiftRepository
     *  The repository containing the custom Shift search methods
     * @param employeeRepository
     *  The repository containing the custom Employee search methods
     */
    public ShiftService (ShiftRepository shiftRepository, EmployeeRepository employeeRepository) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
    }

    /*==================================SEARCH METHODS==================================*/

    /**
     * SEARCH: A search method for finding an Employee using their
     * identification number
     * @param id
     *  The identification number of the Employee being searched for
     * @return
     *  The Employee with the matching ID number if they exist
     * @throws EmployeeNotFoundException
     *  Custom NotFound exception if the Employee is not found
     */
    public Employee findByEmployeeId(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID: " + id + " not found"));
    }

    /**
     * SEARCH: A search method for finding all Shifts for a specific Employee
     * @param employeeId
     *  The ID of the Employee
     * @return
     *  A list of all the Shifts the matching Employee has
     */
    @Transactional
    public List<Shift> findAllEmployeeShifts(Integer employeeId) {
        Employee employee = findByEmployeeId(employeeId);
        return employee.getShifts();
    }

    /**
     * SEARCH: A search method for finding a specific Shift that belongs to an Employee
     * @param employeeId
     *  The ID of the Employee the Shift belongs to
     * @param shiftDate
     *  The date the shift is scheduled
     * @return
     *  The matching Shift if it exists
     * @throws ShiftNotFoundException
     *  Custom RuntimeException if the Shift is not found
     */
    public Shift findEmployeeShiftByDate(Integer employeeId, LocalDate shiftDate) {
        return shiftRepository.findByEmployeeEmployeeIdAndShiftDate(employeeId, shiftDate)
                .orElseThrow(() -> new ShiftNotFoundException("Shift with date" + shiftDate + " not foud"));
    }

    /**
     * SEARCH: A private search method for finding a specific Shift that belongs
     * to an Employee. Used to avoid the RuntimeException throw by findEmployeeShiftByDate
     * @param employeeId
     *  The ID of the Employee the Shift belongs to
     * @param shiftDate
     *  The date the shift is scheduled
     * @return
     *  The matching Shift if it exists
     */
    private Optional<Shift> findOptionalEmployeeShiftByDate(Integer employeeId, LocalDate shiftDate) {return shiftRepository.findByEmployeeEmployeeIdAndShiftDate(employeeId, shiftDate);}

    /**
     * SEARCH: A search method for finding all Shifts for a specific Employee
     * within a week of time
     * @param employeeId
     *  The ID of the Employee
     * @param startingWeekDate
     *  The starting date for Shift searching
     * @return
     *  A list of Shifts that the matching Employee is working within the week
     */
    public List<Shift> findWeeklyEmployeeShifts(Integer employeeId, LocalDate startingWeekDate) {
        employeeRepository.findById(employeeId);
        LocalDate endDate = startingWeekDate.plusDays(6);
        return shiftRepository.findByEmployeeEmployeeIdAndShiftDateBetween(employeeId, startingWeekDate, endDate);
    }

    /**
     * SEARCH: A search method for finding a specific Shift using its identification number
     * @param id
     *  The identification number of the Shift being searched for
     * @return
     *  The Shift with the matching ID if it exists
     * @throws ShiftNotFoundException
     *  Custom RuntimeException if the Shift is not found
     */
    public Shift findById(Integer id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> new ShiftNotFoundException("Shift with ID: " + id + " not found"));
    }

    /**
     * SEARCH: A search method for finding Shift(s) using their duration
     * @param duration
     *  The duration in hours of the Shift(s) being searched for
     * @return
     *  A list of Shift(s) with a matching duration
     */
    public List<Shift> findByDuration(Double duration) {return shiftRepository.findByDuration(duration);}

    /**
     * SEARCH: A search method for finding Shift(s) using their duration
     * and assigned date
     * @param duration
     *  The duration of the shift(s) being searched for
     * @param shiftDate
     *  The assigned date of the Shift(s) being searched for
     * @return
     *  A List of Shift(s) with a matching duration and assigned date
     */
    public List<Shift> findByDurationAndDate(Double duration, LocalDate shiftDate) {return shiftRepository.findByShiftDateAndDuration(shiftDate, duration);}

    /**
     * SEARCH: A search method for finding Shift(s) using their start time
     * @param startTime
     *  The start time of the Shift(s) being searched for
     * @return
     *  A list of Shift(s) with a matching start time
     */
    public List<Shift> findByStartTime(LocalTime startTime) {return shiftRepository.findByStartTime(startTime);}

    /**
     * SEARCH: A search method for finding Shift(s) using their start time
     * and assigned date
     * @param startTime
     *  The start time of the Shift(s) being searched for
     * @param shiftDate
     *  The assigned date of the Shift(s) being searched for
     * @return
     *  A List of Shift(s) with a matching start time and assigned date
     */
    public List<Shift> findByStartTimeAndDate(LocalTime startTime, LocalDate shiftDate) {return shiftRepository.findByShiftDateAndStartTime(shiftDate, startTime);}

    /**
     * SEARCH: A search method for finding Shift(s) using their end time
     * @param endTime
     *  The end time of the Shift(s) being searched for
     * @return
     *  A list of Shift(s) with a matching end time
     */
    public List<Shift> findByEndTime(LocalTime endTime) {return shiftRepository.findByEndTime(endTime);}

    /**
     * SEARCH: A search method for finding Shift(s) using their end time
     * and assigned date
     * @param endTime
     *  The end time of the Shift(s) being searched for
     * @param shiftDate
     *  The assigned date of the Shift(s) being searched for
     * @return
     *  A List of Shift(s) with a matching end time and assigned date
     */
    public List<Shift> findByEndTimeAndDate(LocalTime endTime, LocalDate shiftDate) {return shiftRepository.findByShiftDateAndEndTime(shiftDate, endTime);}

    /**
     * SEARCH: A search method for finding Shift(s) using their shift date
     * @param shiftDate
     *  The date of the Shift(s) being searched for
     * @return
     *  A list of Shift(s) with a matching shift date
     */
    public List<Shift> findByShiftDate(LocalDate shiftDate) {return shiftRepository.findByShiftDate(shiftDate);}

    /**
     * SEARCH: A search method for finding Shift(s) that fall within a given date range
     * @param startDate
     *  The start date of the range being searched
     * @param endDate
     *  The end date of the range being searched
     * @return
     *  A list of Shift(s) whose shift date falls within the given date range
     */
    public List<Shift> findByShiftDateBetween(LocalDate startDate, LocalDate endDate) {return shiftRepository.findByShiftDateBetween(startDate, endDate);}

    /**
     * SEARCH: A search method for finding all Shifts in the database
     * @return
     *  A list of all existing Shifts in the database
     */
    public List<Shift> findAll() {return shiftRepository.findAll();}

    /*==================================LOGIC METHODS==================================*/

    /**
     * CREATE: A logic method that creates a new Shift object, assigns it to an Employee,
     * and saves it to the database
     * @param shiftRequest
     *  The DTO containing all the information for the new Shift
     * @return
     *  A DTO containing a message confirming the Shift was successfully created
     * @throws IllegalActionException
     *  Custom RuntimeException if the Employee already has a shift for the requested day
     */
    public ShiftResponse createShift(ShiftRequest shiftRequest) {
        Employee assignedEmployee = findByEmployeeId(shiftRequest.getEmployeeId());

        //Check to make sure than an employee can only have one shift per day
        if (findOptionalEmployeeShiftByDate(assignedEmployee.getEmployeeId(), shiftRequest.getShiftDate()).isPresent())
            throw new IllegalActionException("Employee " + assignedEmployee.getEmployeeId() +
                                             " already has a shift on " + shiftRequest.getShiftDate() +
                                             ". Either transfer it or delete it before assigning another shift");

        Shift newShift = new Shift(shiftRequest.getStartTime(),
                shiftRequest.getEndTime(),
                shiftRequest.getShiftDate());
        newShift.setEmployee(assignedEmployee);
        shiftRepository.save(newShift);
        return new ShiftResponse("Employee " + assignedEmployee.getEmployeeId() +
                                 " has been assigned a shift on " + shiftRequest.getShiftDate() +
                                 " from " + shiftRequest.getStartTime() +
                                 " to " + shiftRequest.getEndTime());
    }

    /**
     * UPDATE: A logic method that updates an existing Shift's information including
     * its date, start time, end time, and assigned Employee
     * @param shiftUpdate
     *  The DTO containing the updated information for the Shift
     * @return
     *  A DTO containing a message confirming the Shift was successfully updated
     */
    @Transactional
    public ShiftResponse updateShift(ShiftUpdateRequest shiftUpdate) {
        //Validation check
        Shift updatedShift = findEmployeeShiftByDate(shiftUpdate.getEmployeeId(), shiftUpdate.getShiftDate());
        //Updating existing fields
        if (shiftUpdate.getStartTime() != null)
            updatedShift.setStartTime(shiftUpdate.getStartTime());
        if (shiftUpdate.getEndTime() != null)
            updatedShift.setEndTime(shiftUpdate.getEndTime());
        //Re-calculating shift duration
        updatedShift.calculateDuration();
        shiftRepository.save(updatedShift);
        return new ShiftResponse("Successfully updated shift information");
    }

    /**
     * UPDATE: A logic method that transfers an existing Shift to a different Employee
     * @param shiftId
     *  The identification number of the Shift being transferred
     * @param transferEmployeeId
     *  The identification number of the Employee the Shift is being transferred to
     */
    public void transferShift (Integer shiftId, Integer transferEmployeeId) {
        Shift transferShift = findById(shiftId);
        Employee transferEmployee = findByEmployeeId(transferEmployeeId);
        transferShift.setEmployee(transferEmployee);
        shiftRepository.save(transferShift);
    }

    /**
     * DELETE: A logic method that deletes a specific Shift from the database
     * using its identification number
     * @param employeeId
     *  The identification number of the Employee the Shift belongs to
     * @param shiftDate
     *  The date the shift is scheduled
     */
    @Transactional
    public void deleteShift (Integer employeeId, LocalDate shiftDate) {
        Employee employee = findByEmployeeId(employeeId);
        Shift shift = findEmployeeShiftByDate(employeeId, shiftDate); //Validation check

        employee.getShifts().remove(shift);
        shiftRepository.deleteById(shift.getId());
    }

    /**
     * DELETE: A logic method that deletes all Shifts from the database
     */
    public void deleteAllShifts() {shiftRepository.deleteAll();}
}