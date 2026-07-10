package org.example.inventory_manager_exp1.services;

import org.example.inventory_manager_exp1.entities.Employee;
import org.example.inventory_manager_exp1.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class EmployeeServices {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Employee findByUsername(Integer ssn) {
        return employeeRepository.findById(ssn)
                .orElseThrow(() -> new RuntimeException("Employee username not found"));
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee registerEmployee(Employee employee, String unsaltedPassword) {
        employee.setPasswordHash(passwordEncoder.encode(unsaltedPassword));
        return  employeeRepository.save(employee);
    }

    public void deleteEmployee(Integer ssn) {
        employeeRepository.deleteById(ssn);
    }
}
