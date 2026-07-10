package org.example.inventory_manager_beta1.services;

import org.example.inventory_manager_beta1.DTO.Employees.Login.*;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.*;
import org.example.inventory_manager_beta1.DTO.Employees.Update.*;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.EmployeeNotFoundException;
import org.example.inventory_manager_beta1.entities.Employee;
import org.example.inventory_manager_beta1.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * A robust service class to handle all the different logic and search methods relating
 * to the Employee class. It is complete with all the EmployeeRepository search methods
 * defined, and it handles every Employee related logic method with included custom error handling
 */
@Service
public class EmployeeService {
    /**
     * The auto-injected EmployeeRepository for handling custom searching
     */
    @Autowired
    private final EmployeeRepository employeeRepository;

    /**
     * INIT: Used for initializing the employeeRepository for custom database searches
     * @param employeeRepository
     *  The repository containing the custom search methods
     */
    public EmployeeService(EmployeeRepository employeeRepository) {this.employeeRepository = employeeRepository;}

    /**
     * An auto-injected Argon2 PasswordEncoder variable for handling password hashing
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /*==================================SEARCH METHODS==================================*/

    /**
     * SEARCH: A search method for finding an Employee using their
     * identification number
     * @param id
     *  The identification number of the Employee being searched for
     * @return
     *  The Employee with the matching ID number if they exist
     * @throws EmployeeNotFoundException
     *  Custom NotFound exception if the Employee ID search returns null
     */
    public Employee findById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(STR."Employee with ID: \{id} not found"));
    }

    /**
     * SEARCH: A search method for finding an Employee using their Social Security Number
     * @param SSN
     *  The Social Security Number of the Employee being searched for
     * @return
     *  The Employee with the matching SSN if it exists
     * @throws EmployeeNotFoundException
     *  Custom NotFound exception if the Employee SSN search returns null
     */
    public Employee findBySSN(String SSN) {
        return employeeRepository.findBySsn(SSN)
                .orElseThrow(() -> new EmployeeNotFoundException(STR."Employee with SSN: \{SSN} not found"));
    }

    /**
     * SEARCH: A search method for finding Employees using their first name
     * @param firstName
     *  The first name of the Employee(s) being searched for
     * @return
     *  A list of all the Employees with a matching first name
     */
    public List<Employee> findByFirstName(String firstName) {return employeeRepository.findByFirstName(firstName);}

    /**
     * SEARCH: A search method for finding Employees using their last name
     * @param lastName
     *  The last name of the Employee(s) being searched for
     * @return
     *  A list of all the Employees with a matching last name
     */
    public List<Employee> findByLastName(String lastName) {return employeeRepository.findByLastName(lastName);}

    /**
     * SEARCH: A search method for finding an Employee using their username
     * @param userName
     *  The username of the Employee being searched for
     * @return
     *  The Employee with the matching username if they exist
     * @throws EmployeeNotFoundException
     *  Custom NotFound exception if the Employee userName search returns null
     */
    public Employee findByUserName(String userName) {
        return employeeRepository.findByUserName(userName)
                .orElseThrow(() -> new EmployeeNotFoundException(STR."Employee with username: \{userName} not found"));
    }

    /**
     * SEARCH: A search method for finding an Employee using their email
     * @param email
     *  The email of the Employee being searched for
     * @return
     *  The Employee with the matching email if they exist
     * @throws EmployeeNotFoundException
     *  Custom NotFound exception if the Employee email search returns null
     */
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EmployeeNotFoundException(STR."Employee with email: \{email} not found"));
    }

    /**
     * SEARCH: A search method for finding an Employee using their phone number
     * @param phoneNumber
     *  The phone number of the Employee being searched for
     * @return
     *  The Employee with the matching phone number if they exist
     * @throws EmployeeNotFoundException
     *  Custom NotFound exception if the Employee phone number search returns null
     */
    public Employee findByPhoneNumber(String phoneNumber) {
        return employeeRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new EmployeeNotFoundException(STR."Employee with phone number: \{phoneNumber} not found"));
    }

    /**
     * SEARCH: A search method for finding Employees using their access level
     * @param accessLevel
     *  The access level of the Employee(s) being searched for
     * @return
     *  A list of all the Employees with a matching access level
     */
    public List<Employee> findByAccessLevel(AccessLevel accessLevel) {return employeeRepository.findByAccessLevel(accessLevel);}

    /**
     * SEARCH: A search method for finding all Employees in the database
     * @return
     *  A list of all existing Employees in the database
     */
    public List<Employee> findAll() {return employeeRepository.findAll();}

    /*==================================LOGIC METHODS==================================*/

    /**
     * UPDATE: A logic method that saves an Employee to the database
     * @param employee
     *  The Employee being saved
     */
    public void saveEmployee(Employee employee) {employeeRepository.save(employee);}

    /**
     * UPDATE: A logic method that updates an existing Employee's information
     * @param updateRequest
     *  The DTO containing the new information for the Employee
     */
    public UpdateResponse updateEmployee(UpdateRequest updateRequest) {
        Employee updatedEmployee = findById(updateRequest.getEmployeeId());
        updatedEmployee.setFirstName(updateRequest.getFirstName());
        updatedEmployee.setLastName(updateRequest.getLastName());
        updatedEmployee.setUserName(updateRequest.getUserName());
        updatedEmployee.setEmail(updateRequest.getEmail());
        updatedEmployee.setPhoneNumber(updateRequest.getPhoneNumber());
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isBlank()) {
            updatedEmployee.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        //Save updated employee
        employeeRepository.save(updatedEmployee);
        //Return UpdateResponse DTO for front end use
        return new UpdateResponse(updatedEmployee.getEmployeeId(), updatedEmployee.getUserName());
    }

    /**
     * CREATE: A logic method that creates a new Employee object, automatically
     * hashes their password using Argon2, assigns an access level, and adds it
     * to the database
     * @param signUpRequest
     *  The DTO containing all the Employee's information
     */
    public SignUpResponse registerEmployee(SignUpRequest signUpRequest) {
        Employee newEmployee = new Employee(signUpRequest.getSsn(),
                                         signUpRequest.getFirstName(),
                                         signUpRequest.getLastName(),
                                         signUpRequest.getUserName(),
                                         signUpRequest.getEmail(),
                                         signUpRequest.getPhoneNumber());
        newEmployee.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        employeeRepository.save(newEmployee);
        //Check to make sure the first person added to the system
        //has access to every function
        if (newEmployee.getEmployeeId() == 1) {
            newEmployee.setAccessLevel(AccessLevel.GENERAL_MANAGER);
        }
        else {
            newEmployee.setAccessLevel(AccessLevel.EMPLOYEE);
        }
        return new SignUpResponse(newEmployee.getEmployeeId(), newEmployee.getFirstName(), newEmployee.getAccessLevel());
    }

    /**
     * DELETE: A logic method that deletes an Employee from the database using their
     * employeeId
     * @param employeeSSN
     *  The SSN of the Employee being deleted
     */
    public void deleteEmployee(String employeeSSN) {
        Employee employee = findBySSN(employeeSSN);
        employeeRepository.deleteById(employee.getEmployeeId());
    }

    /**
     * VERIFY: A logic method to verify if the incoming username and password match the
     * username and password stored for the Employee
     * @param loginAttempt
     *  The DTO containing the username and password
     * @return
     *  A DTO containing a message regarding the status of the login
     */
    public LoginResponse employeeLogin(LoginRequest loginAttempt) {
        Employee employeeLogin = findByUserName(loginAttempt.getUserName());
        if (!passwordEncoder.matches(loginAttempt.getPassword(), employeeLogin.getPassword())) {
            return new LoginResponse("Incorrect password. Please try again");
        }
        return new LoginResponse(STR."Successfully logged in as \{loginAttempt.getUserName()}");
    }
}
