package org.example.inventory_manager_beta1.repositories;

import jakarta.annotation.Nonnull;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * A repository containing all the custom database searches relevant to the Employee class.
 * All Optional wrapped search methods use the custom Employee Exception handler for better
 * HTTP Exception handling
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    /**
     * A JpaRepository search method (overridden to use custom Exception handling) to
     * search a database for an Employee using their identification number
     * (assigned at Employee creation)
     * @param id
     *  The identification number of the Employee being searched for
     * @return
     *  The Employee with the matching ID if they exist
     */
    @Override
    @Nonnull Optional<Employee> findById(@Nonnull Integer id);

    /**
     * A custom JpaRepository search method to search a database for an Employee
     * using their Social Security Number
     * @param ssn
     *  The Social Security Number of the Employee being searched for
     * @return
     *  The Employee with the matching SSN if they exist
     */
    Optional<Employee> findBySsn (String ssn);

    /**
     * A custom JpaRepository search method to search a database for Employee(s)
     * using their first name
     * @param firstName
     *  The first name of the Employee(s) being searched for
     * @return
     *  A list of Employee(s) with a matching first names
     */
    List<Employee> findByFirstName (String firstName);

    /**
     * A custom JpaRepository search method to search a database for Employee(s)
     * using their last name
     * @param lastName
     *  The last name of the Employee(s) being searched for
     * @return
     *  A list of Employee(s) with a matching last names
     */
    List<Employee> findByLastName (String lastName);

    /**
     * A custom JpaRepository search method to search a database for an Employee
     * using their username
     * @param userName
     *  The username of the Employee being searched for
     * @return
     *  The Employee with the matching username if they exist
     */
    Optional<Employee> findByUserName(String userName);

    /**
     * A custom JpaRepository search method to search a database for an Employee
     * using their email address
     * @param email
     *  The email of the Employee being searched for
     * @return
     *  The Employee with the matching email if they exist
     */
    Optional<Employee> findByEmail (String email);

    /**
     * A custom JpaRepository search method to search a database for an Employee
     * using their phone number
     * @param phoneNumber
     *  The phone number of the Employee being searched for
     * @return
     *  The Employee with the matching phone number if they exist
     */
    Optional<Employee> findByPhoneNumber (String phoneNumber);

    /**
     * A custom JpaRepository search method to search a database for Employee(s)
     * using their access level
     * @param accessLevel
     *  The access level of the Employee(s) being searched for
     * @return
     *  A list of Employee(s) with a matching access level
     */
    List<Employee> findByAccessLevel (AccessLevel accessLevel);
}
