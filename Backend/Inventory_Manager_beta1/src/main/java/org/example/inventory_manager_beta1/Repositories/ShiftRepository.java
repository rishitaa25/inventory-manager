package org.example.inventory_manager_beta1.Repositories;

import jakarta.annotation.Nonnull;
import org.example.inventory_manager_beta1.Entities.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.ShiftNotFoundException;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * A repository containing all the custom database searches relevant to the Shift class.
 * All Optional wrapped search methods use the custom Shift Exception handler for better
 * HTTP Exception handling
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    /**
     * A JpaRepository search method (overridden to use custom Exception handling) to
     * search a database for a Shift using its identification number
     * (assigned at Shift creation)
     * @param id
     *  The identification number of the Shift being searched for
     * @return
     *  The Shift with the matching ID if it exists
     * @throws ShiftNotFoundException
     *  A custom RuntimeException thrown when the Shift is not found
     */
    @Override
    @Nonnull Optional<Shift> findById (@Nonnull Integer id);

    /**
     * A custom JpaRepository search method to search a database for Shift(s)
     * using their duration
     * @param duration
     *  The duration in hours of the Shift(s) being searched for
     * @return
     *  A list of Shift(s) with a matching duration
     */
    List<Shift> findByDuration (Double duration);

    /**
     * A custom JpaRepository search method to search a database for Shift(s)
     * using their start time
     * @param startTime
     *  The start time of the Shift(s) being searched for
     * @return
     *  A list of Shift(s) with a matching start time
     */
    List<Shift> findByStartTime (LocalTime startTime);

    /**
     * A custom JpaRepository search method to search a database for Shift(s)
     * using their end time
     * @param endTime
     *  The end time of the Shift(s) being searched for
     * @return
     *  A list of Shift(s) with a matching end time
     */
    List<Shift> findByEndTime (LocalTime endTime);

    /**
     * A custom JpaRepository search method to search a database for Shift(s)
     * using their shift date
     * @param shiftDate
     *  The date of the Shift(s) being searched for
     * @return
     *  A list of Shift(s) with a matching shift date
     */
    List<Shift> findByShiftDate (LocalDate shiftDate);

    /**
     * A custom JpaRepository search method to search a database for Shift(s)
     * using their assigned date and duration
     * @param shiftDate
     *  The date the Shift is assigned
     * @param duration
     *  The length of the Shift
     * @return
     *  A List of Shift(s) with a matching assigned date and duration
     */
    List<Shift> findByShiftDateAndDuration (LocalDate shiftDate, Double duration);

    /**
     A custom JpaRepository search method to search a database for Shift(s)
     * using their assigned date and start time
     * @param shiftDate
     *  The date the Shift is assigned
     * @param startTime
     *  The start time of the Shift
     * @return
     *  A List of Shift(s) with a matching assigned date and start time
     */
    List<Shift> findByShiftDateAndStartTime (LocalDate shiftDate, LocalTime startTime);

    /**
     A custom JpaRepository search method to search a database for Shift(s)
     * using their assigned date and end time
     * @param shiftDate
     *  The date the Shift is assigned
     * @param endTime
     *  The end time of the Shift
     * @return
     *  A List of Shift(s) with a matching assigned date and end time
     */
    List<Shift> findByShiftDateAndEndTime (LocalDate shiftDate, LocalTime endTime);

    /**
     * A custom JpaRepository search method to search a database for Shift(s)
     * that fall within a given date range
     * @param startDate
     *  The start date of the range being searched
     * @param endDate
     *  The end date of the range being searched
     * @return
     *  A list of Shift(s) whose shift date falls within the given date range
     */
    List<Shift> findByShiftDateBetween (LocalDate startDate, LocalDate endDate);

    /**
     * A custom JpaRepository search method to search a database for Shift(s)
     * belonging to a specific Employee that fall within a given date range
     * @param employeeId
     *  The identification number of the Employee whose shifts are being searched for
     * @param startDate
     *  The start date of the range being searched
     * @param endDate
     *  The end date of the range being searched
     * @return
     *  A list of Shift(s) belonging to the matching Employee within the given date range
     */
    List<Shift> findByEmployeeEmployeeIdAndShiftDateBetween(Integer employeeId, LocalDate startDate, LocalDate endDate);

    /**
     * A custom JpaRepository search method to search a database for a Shift
     * using its date that belongs to a specific Employee
     * @param employeeId
     *  The ID of the Employee that the Shift belongs to
     * @param shiftDate
     *  The date the Shift is scheduled
     * @return
     *  The Shift with the matching date if it exists
     * @throws ShiftNotFoundException
     *  A custom RuntimeException thrown if the Shift is not found
     */
    Optional<Shift> findByEmployeeEmployeeIdAndShiftDate(Integer employeeId, LocalDate shiftDate);
}