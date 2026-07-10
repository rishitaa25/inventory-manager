package org.example.inventory_manager_beta1.Repositories;

import jakarta.annotation.Nonnull;
import org.example.inventory_manager_beta1.Entities.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * A repository containing all the custom database searches relevant to the Shipment class.
 * All Optional wrapped search methods use the custom Shipment Exception handler for better
 * HTTP Exception handling
 */
@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    /**
     * A JpaRepository search method (overridden to use custom Exception handling) to
     * search a database for a Shipment using their identification number
     * (assigned at Shipment creation)
     * @param id
     *  The identification number of the Shipment being searched for
     * @return
     *  The Shipment with the matching ID if it exists
     */
    @Override
    @Nonnull Optional<Shipment> findById(@Nonnull Integer id);

    /**
     * A custom JpaRepository search method to search a database for Shipment(s)
     * assigned to a Driver using their employee identification number
     * @param employeeId
     *  The employee identification number of the Driver being searched for
     * @return
     *  A list of Shipment(s) assigned to the Driver with the matching employee ID
     */
    List<Shipment> findByDrivers_EmployeeId(Integer employeeId);

    /**
     * A custom JpaRepository search method to search a database for Shipment(s)
     * using their arrival status
     * @param arrived
     *  The arrival status of the Shipment(s) being searched for
     * @return
     *  A list of Shipment(s) with a matching arrival status
     */
    List<Shipment> findByArrived(Boolean arrived);

    /**
     * A custom JpaRepository search method to search a database for Shipment(s)
     * using their scheduled delivery date
     * @param arrivalDate
     *  The delivery date of the Shipment(s) being searched for
     * @return
     *  A list of Shipment(s) with a matching delivery date
     */
    List<Shipment> findByArrivalDate(LocalDate arrivalDate);
}