package org.example.inventory_manager_beta1.Repositories;

import jakarta.annotation.Nonnull;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.DataEnums.ManagementTitle;
import org.example.inventory_manager_beta1.Entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * A repository containing all the custom database searches relevant to the Admin class.
 * All Optional wrapped search methods use the custom Admin Exception handler for better
 * HTTP Exception handling
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    /**
     * A JpaRepository search method (overridden to use custom Exception handling) to
     * search a database for an Admin using their identification number
     * (assigned at Admin creation)
     * @param id
     *  The identification number of the Admin being searched for
     * @return
     *  The Admin with the matching ID if they exist
     */
    @Override
    @Nonnull Optional<Admin> findById(@Nonnull Integer id);

    /**
     * A custom JpaRepository search method to search a database for an Admin
     * using their Social Security Number
     * @param ssn
     *  The Social Security Number of the Admin being searched for
     * @return
     *  The Admin with the matching SSN if they exist
     */
    Optional<Admin> findBySsn(String ssn);

    /**
     * A custom JpaRepository search method to search a database for Admin(s)
     * using their first name
     * @param firstName
     *  The first name of the Admin(s) being searched for
     * @return
     *  A list of Admin(s) with a matching first name
     */
    List<Admin> findByFirstName(String firstName);

    /**
     * A custom JpaRepository search method to search a database for Admin(s)
     * using their last name
     * @param lastName
     *  The last name of the Admin(s) being searched for
     * @return
     *  A list of Admin(s) with a matching last name
     */
    List<Admin> findByLastName(String lastName);

    /**
     * A custom JpaRepository search method to search a database for an Admin
     * using their username
     * @param userName
     *  The username of the Admin being searched for
     * @return
     *  The Admin with the matching username if they exist
     */
    Optional<Admin> findByUserName(String userName);

    /**
     * A custom JpaRepository search method to search a database for an Admin
     * using their email address
     * @param email
     *  The email of the Admin being searched for
     * @return
     *  The Admin with the matching email if they exist
     */
    Optional<Admin> findByEmail(String email);

    /**
     * A custom JpaRepository search method to search a database for an Admin
     * using their phone number
     * @param phoneNumber
     *  The phone number of the Admin being searched for
     * @return
     *  The Admin with the matching phone number if they exist
     */
    Optional<Admin> findByPhoneNumber(String phoneNumber);

    /**
     * A custom JpaRepository search method to search a database for Employee(s)
     * using their access level
     * @param accessLevel
     *  The access level of the Employee(s) being searched for
     * @return
     *  A list of Employee(s) with a matching access level
     */
    List<Admin> findByAccessLevel (AccessLevel accessLevel);

    /**
     * A custom JpaRepository search method to search a database for Admin(s)
     * using their management title
     * @param managementTitle
     *  The management title of the Admin(s) being searched for
     * @return
     *  A list of Admin(s) with a matching management title
     */
    List<Admin> findByManagementTitle(ManagementTitle managementTitle);
}
