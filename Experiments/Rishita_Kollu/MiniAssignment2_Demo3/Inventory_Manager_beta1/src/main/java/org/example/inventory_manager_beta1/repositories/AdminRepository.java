package org.example.inventory_manager_beta1.repositories;

import jakarta.annotation.Nonnull;
import org.example.inventory_manager_beta1.DataEnums.ManagementTitle;
import org.example.inventory_manager_beta1.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    @Override
    @Nonnull Optional<Admin> findById (@Nonnull Integer id);
    Optional<Admin> findBySsn (String ssn);
    List<Admin> findByFirstName (String firstName);
    List<Admin> findByLastName (String lastName);
    Optional<Admin> findByUserName (String userName);
    Optional<Admin> findByEmail (String email);
    Optional<Admin> findByPhoneNumber (String phoneNumber);
    List<Admin> findByManagementTitle (ManagementTitle managementTitle);
}
