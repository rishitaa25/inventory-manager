package org.example.inventory_manager_exp1.repositories;

import org.example.inventory_manager_exp1.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByFirstName(String firstName);
    List<Employee> findByAccessLevel (Integer accessLevel);
}
