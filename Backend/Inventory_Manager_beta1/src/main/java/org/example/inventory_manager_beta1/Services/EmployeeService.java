package org.example.inventory_manager_beta1.Services;

import org.example.inventory_manager_beta1.Configuration.SessionToken.JwtUtil;
import org.example.inventory_manager_beta1.DTO.Employees.Login.*;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.*;
import org.example.inventory_manager_beta1.DTO.Employees.Update.*;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.EmployeeNotFoundException;
import org.example.inventory_manager_beta1.Entities.Employee;
import org.example.inventory_manager_beta1.Entities.Session;
import org.example.inventory_manager_beta1.Repositories.AdminRepository;
import org.example.inventory_manager_beta1.Repositories.DriverRepository;
import org.example.inventory_manager_beta1.Repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * A robust service class to handle all the different logic and search methods relating
 * to the Employee class. It is complete with all the EmployeeRepository search methods
 * defined, and it handles every Employee related logic method with included custom error handling
 */
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DriverRepository driverRepository;
    private final AdminRepository adminRepository;
    private final SessionService sessionService;

    /**
     * Lazily injected to avoid a circular dependency:
     * EmployeeService -> NotificationService -> EmployeeService.
     * {@code @Lazy} breaks the cycle by deferring NotificationService's initialization.
     */
    @Autowired
    @Lazy
    private NotificationService notificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * INIT: Used for initializing the repositories for custom database checks and searches
     * @param employeeRepository
     *  The repository containing the custom Employee search methods
     * @param driverRepository
     *  The repository containing the custom Driver search methods
     * @param adminRepository
     *  The repository containing the custom Admin search methods
     * @param sessionService
     *  The service for managing user sessions
     */
    public EmployeeService(EmployeeRepository employeeRepository, DriverRepository driverRepository, AdminRepository adminRepository, SessionService sessionService) {
        this.employeeRepository = employeeRepository;
        this.driverRepository = driverRepository;
        this.adminRepository = adminRepository;
        this.sessionService = sessionService;
    }

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
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID: " + id + " not found"));
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
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with SSN: " + SSN + " not found"));
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
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with username: " + userName + " not found"));
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
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with email: " + email + " not found"));
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
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with phone number: " + phoneNumber + " not found"));
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
     * @param employeeUpdateRequest
     *  The DTO containing the new information for the Employee
     */
    public void updateEmployee(UpdateEmployeeRequest employeeUpdateRequest) {
        Employee updatedEmployee = findById(employeeUpdateRequest.getEmployeeId());
        if (employeeUpdateRequest.getFirstName() != null)
            updatedEmployee.setFirstName(employeeUpdateRequest.getFirstName());
        if (employeeUpdateRequest.getLastName() != null)
            updatedEmployee.setLastName(employeeUpdateRequest.getLastName());
        if (employeeUpdateRequest.getUserName() != null)
            updatedEmployee.setUserName(employeeUpdateRequest.getUserName());
        if (employeeUpdateRequest.getEmail() != null)
            updatedEmployee.setEmail(employeeUpdateRequest.getEmail());
        if (employeeUpdateRequest.getPhoneNumber() != null)
            updatedEmployee.setPhoneNumber(employeeUpdateRequest.getPhoneNumber());
        if (employeeUpdateRequest.getPassword() != null && !employeeUpdateRequest.getPassword().isBlank()) {
            updatedEmployee.setPassword(passwordEncoder.encode(employeeUpdateRequest.getPassword()));
        }
        saveEmployee(updatedEmployee);
    }

    /**
     * REQUEST: Submits an employee-initiated update request for admin approval.
     * <p>Instead of applying changes directly, this passes the request to
     * {@link NotificationService#createUpdateNotification(UpdateEmployeeRequest, String)},
     * which stores it as a PENDING notification sent to admins. No changes
     * are applied to the employee's record until an admin approves it.
     * @param updateRequest
     *  The DTO containing the employee's requested changes
     * @return
     *  A message confirming the request was submitted for review
     */
    public String requestUpdate(UpdateEmployeeRequest updateRequest) {
        findById(updateRequest.getEmployeeId());
        notificationService.createUpdateNotification(updateRequest, "EMPLOYEE");
        return "Update request submitted successfully. An admin will review your request.";
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
        if (employeeRepository.count() == 0 && driverRepository.count() == 0 && adminRepository.count() == 0) {
            newEmployee.setAccessLevel(AccessLevel.GENERAL_MANAGER);
        }
        else {
            newEmployee.setAccessLevel(AccessLevel.EMPLOYEE);
        }
        saveEmployee(newEmployee);
        return new SignUpResponse(newEmployee.getEmployeeId(), newEmployee.getFirstName(), newEmployee.getUserName(), newEmployee.getAccessLevel());
    }

    /**
     * DELETE: A logic method that deletes an Employee from the database using their
     * employeeId
     * @param employeeId
     *  The ID of the Employee being deleted
     */
    public void deleteEmployee(Integer employeeId) {
        //Validation check
        findById(employeeId);
        employeeRepository.deleteById(employeeId);
    }

    /**
     * DELETE: A logic method that deletes all Employees from the database
     */
    public void deleteAllEmployees() {employeeRepository.deleteAll();}

    /**
     * VERIFY: Validates credentials, opens a new session, and returns a JWT
     * containing the session ID.
     * @param loginAttempt
     *  The DTO containing the username and password
     * @return
     *  A LoginResponse with a real JWT token on success, error message on failure
     */
    public LoginResponse employeeLogin(LoginRequest loginAttempt) {
        Optional<Employee> employeeOpt = employeeRepository.findByUserName(loginAttempt.getUserName());

        if (employeeOpt.isEmpty()) {
            return new LoginResponse("User not found", null, null, null, null, null);
        }

        Employee employee = employeeOpt.get();

        if (employee.getPassword() == null || !passwordEncoder.matches(loginAttempt.getPassword(), employee.getPassword())) {
            return new LoginResponse("Invalid password", null, null, null, null, null);
        }

        Session session = sessionService.createSession(
                employee.getEmployeeId(),
                employee.getAccessLevel(),
                employee.getUserName()
        );

        String token = jwtUtil.generateToken(session.getSessionId());

        return new LoginResponse(
                "Login successful",
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getAccessLevel().name(),
                token
        );
    }

    /**
     * LOGOUT: Accepts the raw Authorization header, extracts the session ID from
     * the JWT, and deletes the session row which immediately invalidating the token.
     * @param authHeader
     *  The raw Authorization header value (e.g. "Bearer &lt;token&gt;")
     * @return
     *  Confirmation message on success, error message if header is missing
     */
    public String employeeLogout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "No active session to log out of.";
        }
        String token = authHeader.substring(7);
        String sessionId = jwtUtil.getSessionId(token);
        sessionService.closeSession(sessionId);
        return "Logged out successfully.";
    }
}
