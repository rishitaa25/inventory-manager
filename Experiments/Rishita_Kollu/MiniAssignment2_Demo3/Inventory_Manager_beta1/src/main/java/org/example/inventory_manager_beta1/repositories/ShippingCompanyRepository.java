package org.example.inventory_manager_beta1.repositories;

import org.example.inventory_manager_beta1.entities.ShippingCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ShippingCompanyRepository extends JpaRepository<ShippingCompany, Integer> {
    Optional<ShippingCompany> findByShippingCompanyName(String companyName);
    Optional<ShippingCompany> findByShippingCompanyEmail(String companyEmail);
    Optional<ShippingCompany> findByShippingCompanyPhone(String companyPhone);
}
