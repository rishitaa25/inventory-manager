package org.example.inventory_manager_beta1.Repositories;

import jakarta.annotation.Nonnull;
import org.example.inventory_manager_beta1.Entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * A repository containing all the custom database searches relevant to the Driver class.
 * All Optional wrapped search methods use the custom Driver Exception handler for better
 * HTTP Exception handling
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    /**
     * A JpaRepository search method (overridden to use custom Exception handling) to
     * search a database for a Driver using their identification number
     * (assigned at Driver creation)
     * @param id
     *  The identification number of the Driver being searched for
     * @return
     *  The Driver with the matching ID if they exist
     */
    @Override
    @Nonnull Optional<Driver> findById(@Nonnull Integer id);

    /**
     * A custom JpaRepository search method to search a database for a Driver
     * using their Social Security Number
     * @param ssn
     *  The Social Security Number of the Driver being searched for
     * @return
     *  The Driver with the matching SSN if they exist
     */
    Optional<Driver> findBySsn(String ssn);

    /**
     * A custom JpaRepository search method to search a database for Driver(s)
     * using their first name
     * @param firstName
     *  The first name of the Driver(s) being searched for
     * @return
     *  A list of Driver(s) with a matching first name
     */
    List<Driver> findByFirstName(String firstName);

    /**
     * A custom JpaRepository search method to search a database for Driver(s)
     * using their last name
     * @param lastName
     *  The last name of the Driver(s) being searched for
     * @return
     *  A list of Driver(s) with a matching last name
     */
    List<Driver> findByLastName(String lastName);

    /**
     * A custom JpaRepository search method to search a database for a Driver
     * using their username
     * @param userName
     *  The username of the Driver being searched for
     * @return
     *  The Driver with the matching username if they exist
     */
    Optional<Driver> findByUserName(String userName);

    /**
     * A custom JpaRepository search method to search a database for a Driver
     * using their phone number
     * @param phoneNumber
     *  The phone number of the Driver being searched for
     * @return
     *  The Driver with the matching phone number if they exist
     */
    Optional<Driver> findByPhoneNumber(String phoneNumber);

    /**
     * A custom JpaRepository search method to search a database for a Driver
     * using their email address
     * @param email
     *  The email of the Driver being searched for
     * @return
     *  The Driver with the matching email if they exist
     */
    Optional<Driver> findByEmail(String email);
}
