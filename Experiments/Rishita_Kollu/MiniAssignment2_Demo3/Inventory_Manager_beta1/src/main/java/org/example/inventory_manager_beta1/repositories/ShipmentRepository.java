package org.example.inventory_manager_beta1.repositories;

import org.example.inventory_manager_beta1.entities.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    List<Shipment> findByDrivers_EmployeeId (Integer employeeId);
    List<Shipment> findByArrived (Boolean arrived);
    List<Shipment> findByDeliveryDate(LocalDate deliveryDay);
}
