package org.example.inventory_manager_beta1.Repositories;

import jakarta.annotation.Nonnull;
import org.example.inventory_manager_beta1.Entities.ShippingCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * A repository containing all the custom database searches relevant to the ShippingCompany
 * class. All Optional wrapped search methods use the custom Employee Exception handler for
 * better HTTP Exception handling
 */
@Repository
public interface ShippingCompanyRepository extends JpaRepository<ShippingCompany, Integer> {
    /**
     * A JpaRepository search method (overridden to use custom Exception handling) to
     * search a database for a ShippingCompany using their identification number
     * (assigned at ShippingCompany creation)
     * @param id
     *  The identification number of the ShippingCompany being searched for
     * @return
     *  The ShippingCompany with the matching ID if it exists
     */
    @Override
    @Nonnull Optional<ShippingCompany> findById(@Nonnull Integer id);

    /**
     * A custom JpaRepository search method to search a database for a ShippingCompany
     * using their company name
     * @param companyName
     *  The company name of the ShippingCompany being searched for
     * @return
     *  The ShippingCompany with the matching company name if they exist
     */
    Optional<ShippingCompany> findByShippingCompanyName(String companyName);

    /**
     * A custom JpaRepository search method to search a database for a ShippingCompany
     * using their email address
     * @param companyEmail
     *  The email address of the ShippingCompany being searched for
     * @return
     *  The ShippingCompany with the matching email address if they exist
     */
    Optional<ShippingCompany> findByShippingCompanyEmail(String companyEmail);

    /**
     * A custom JpaRepository search method to search a database for a ShippingCompany
     * using their phone number
     * @param companyPhone
     *  The phone number of the ShippingCompany being searched for
     * @return
     *  The ShippingCompany with the matching phone number if they exist
     */
    Optional<ShippingCompany> findByShippingCompanyPhone(String companyPhone);
}