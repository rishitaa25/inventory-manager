package org.example.inventory_manager_exp1.repositories;

import org.example.inventory_manager_exp1.entities.Delivery;
import org.example.inventory_manager_exp1.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Employee, Integer> {
    Optional<Delivery> findByDriverName(String driverName);
    List<Delivery> findByAccessLevel (Integer accessLevel);
    Optional<Delivery> findByCompanyName(String companyName);
    Optional<Delivery> findByDeliveryDay(String deliveryDay);
}
