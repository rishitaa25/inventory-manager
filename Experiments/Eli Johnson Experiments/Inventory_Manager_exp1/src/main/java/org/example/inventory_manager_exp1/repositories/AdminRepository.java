package org.example.inventory_manager_exp1.repositories;

import org.example.inventory_manager_exp1.entities.Admin;
import org.example.inventory_manager_exp1.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Employee, Integer> {
    Optional<Admin> findByFirstName(String firstName);
    Optional<Admin> findByLastName(String lastName);
    List<Employee> findByAccessLevel (Integer accessLevel);
}
