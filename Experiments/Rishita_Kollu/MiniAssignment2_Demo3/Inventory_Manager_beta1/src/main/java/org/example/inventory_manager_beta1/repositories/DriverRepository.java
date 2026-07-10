package org.example.inventory_manager_beta1.repositories;

import org.example.inventory_manager_beta1.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Optional<Driver> findBySsn (String ssn);
    List<Driver> findByFirstName (String firstName);
    List<Driver> findByLastName (String lastName);
    Optional<Driver> findByUserName (String userName);
    Optional<Driver> findByPhoneNumber (String phoneNumber);
    Optional<Driver> findByEmail (String email);
}
