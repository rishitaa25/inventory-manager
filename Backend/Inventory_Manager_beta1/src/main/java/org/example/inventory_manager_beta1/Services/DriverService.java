package org.example.inventory_manager_beta1.Services;

import org.example.inventory_manager_beta1.Configuration.SessionToken.JwtUtil;
import org.example.inventory_manager_beta1.DTO.Employees.Login.*;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.*;
import org.example.inventory_manager_beta1.DTO.Employees.Update.*;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.DriverNotFoundException;
import org.example.inventory_manager_beta1.Entities.Driver;
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
 * to the Driver class. It is complete with all the DriverRepository search methods
 * defined, and it handles every Driver related logic method with included custom error handling
 */
@Service
public class DriverService {

    private final EmployeeRepository employeeRepository;
    private final DriverRepository driverRepository;
    private final AdminRepository adminRepository;
    private final SessionService sessionService;

    /**
     * Lazily injected to avoid a circular dependency:
     * DriverService -> NotificationService -> EmployeeService -> NotificationService.
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
     * The repository containing the custom Driver search methods
     * @param adminRepository
     * The repository containing the custom Admin search methods
     */
    public DriverService(EmployeeRepository employeeRepository, DriverRepository driverRepository, AdminRepository adminRepository, SessionService sessionService) {
        this.employeeRepository = employeeRepository;
        this.driverRepository = driverRepository;
        this.adminRepository = adminRepository;
        this.sessionService = sessionService;
    }

    /*==================================SEARCH METHODS==================================*/

    /**
     * SEARCH: A search method for finding a Driver using their identification number
     * @param id
     *  The identification number of the Driver being searched for
     * @return
     *  The Driver with the matching ID number if they exist
     * @throws DriverNotFoundException
     *  Custom NotFound exception if the Driver ID search returns null
     */
    public Driver findById(Integer id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver with ID: " + id + " not found"));
    }

    /**
     * SEARCH: A search method for finding a Driver using their Social Security Number
     * @param ssn
     *  The Social Security Number of the Driver being searched for
     * @return
     *  The Driver with the matching SSN if they exist
     * @throws DriverNotFoundException
     *  Custom NotFound exception if the Driver SSN search returns null
     */
    public Driver findBySSN(String ssn) {
        return driverRepository.findBySsn(ssn)
                .orElseThrow(() -> new DriverNotFoundException("Driver with SSN: " + ssn + " not found"));
    }

    /**
     * SEARCH: A search method for finding all Drivers in the database
     * @return
     *  A list of all existing Drivers in the database
     */
    public List<Driver> findAll() {return driverRepository.findAll();}

    /**
     * SEARCH: A search method for finding all Drivers matching a list of identification numbers
     * @param driverIds
     *  The list of identification numbers of the Drivers being searched for
     * @return
     *  A list of all Drivers with a matching identification number
     */
    public List<Driver> findAllById(List<Integer> driverIds) {return driverRepository.findAllById(driverIds);}

    /**
     * SEARCH: A search method for finding Driver(s) using their first name
     * @param firstName
     *  The first name of the Driver(s) being searched for
     * @return
     *  A list of all Drivers with a matching first name
     */
    public List<Driver> findByFirstName(String firstName) {return driverRepository.findByFirstName(firstName);}

    /**
     * SEARCH: A search method for finding Driver(s) using their last name
     * @param lastName
     *  The last name of the Driver(s) being searched for
     * @return
     *  A list of all Drivers with a matching last name
     */
    public List<Driver> findByLastName(String lastName) {return driverRepository.findByLastName(lastName);}

    /**
     * SEARCH: A search method for finding a Driver using their username
     * @param userName
     *  The username of the Driver being searched for
     * @return
     *  The Driver with the matching username if they exist
     * @throws DriverNotFoundException
     *  Custom NotFound exception if the Driver username search returns null
     */
    public Driver findByUserName(String userName) {
        return driverRepository.findByUserName(userName)
                .orElseThrow(() -> new DriverNotFoundException("Driver with username: " + userName + " not found"));
    }

    /**
     * SEARCH: A search method for finding a Driver using their email address
     * @param email
     *  The email of the Driver being searched for
     * @return
     *  The Driver with the matching email if they exist
     * @throws DriverNotFoundException
     *  Custom NotFound exception if the Driver email search returns null
     */
    public Driver findByEmail(String email) {
        return driverRepository.findByEmail(email)
                .orElseThrow(() -> new DriverNotFoundException("Driver with email: " + email + " not found"));
    }

    /**
     * SEARCH: A search method for finding a Driver using their phone number
     * @param phoneNumber
     *  The phone number of the Driver being searched for
     * @return
     *  The Driver with the matching phone number if they exist
     * @throws DriverNotFoundException
     *  Custom NotFound exception if the Driver phone number search returns null
     */
    public Driver findByPhoneNumber(String phoneNumber) {
        return driverRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DriverNotFoundException("Driver with phone number: " + phoneNumber + " not found"));
    }

    /*==================================LOGIC METHODS==================================*/

    /**
     * CREATE: A logic method that creates a new Driver object, automatically
     * hashes their password using Argon2, assigns an access level, and adds it
     * to the database
     * @param driverSignUp
     *  The DTO containing all the Driver's information
     * @return
     *  A DTO containing the new Driver's ID, first name, and access level
     */
    public SignUpResponse registerDriver(SignUpRequest driverSignUp) {
        Driver newDriver = new Driver(
                driverSignUp.getSsn(),
                driverSignUp.getFirstName(),
                driverSignUp.getLastName(),
                driverSignUp.getUserName(),
                driverSignUp.getEmail(),
                driverSignUp.getPhoneNumber()
        );
        newDriver.setPassword(passwordEncoder.encode(driverSignUp.getPassword()));
        //Check to make sure the very first person added to the system
        //has access to every function
        if (employeeRepository.count() == 0 && driverRepository.count() == 0 && adminRepository.count() == 0) {
            newDriver.setAccessLevel(AccessLevel.GENERAL_MANAGER);
        }
        else {
            newDriver.setAccessLevel(AccessLevel.DRIVER);
        }
        saveDriver(newDriver);
        return new SignUpResponse(newDriver.getEmployeeId(), newDriver.getFirstName(), newDriver.getUserName(), newDriver.getAccessLevel());
    }

    /**
     * UPDATE: A logic method that saves a Driver to the database
     * @param driver
     *  The Driver being saved
     */
    public void saveDriver(Driver driver) {driverRepository.save(driver);}

    /**
     * UPDATE: A logic method that updates an existing Driver's information
     * @param driverUpdateRequest
     *  The DTO containing the new information for the Driver
     * @return
     *  A DTO containing the updated Driver's ID and username
     */
    public UpdateEmployeeResponse updateDriver(UpdateEmployeeRequest driverUpdateRequest) {
        Driver updateDriver = findById(driverUpdateRequest.getEmployeeId());
        if (driverUpdateRequest.getFirstName() != null)
            updateDriver.setFirstName(driverUpdateRequest.getFirstName());
        if (driverUpdateRequest.getLastName() != null)
            updateDriver.setLastName(driverUpdateRequest.getLastName());
        if (driverUpdateRequest.getUserName() != null)
            updateDriver.setUserName(driverUpdateRequest.getUserName());
        if (driverUpdateRequest.getEmail() != null)
            updateDriver.setEmail(driverUpdateRequest.getEmail());
        if (driverUpdateRequest.getPhoneNumber() != null)
            updateDriver.setPhoneNumber(driverUpdateRequest.getPhoneNumber());
        if (driverUpdateRequest.getPassword() != null && !driverUpdateRequest.getPassword().isBlank()) {
            updateDriver.setPassword(passwordEncoder.encode(driverUpdateRequest.getPassword()));
        }
        driverRepository.save(updateDriver);
        return new UpdateEmployeeResponse(updateDriver.getEmployeeId(), updateDriver.getUserName());
    }

    public String requestUpdate(UpdateEmployeeRequest updateRequest) {
        findById(updateRequest.getEmployeeId());
        notificationService.createUpdateNotification(updateRequest, "DRIVER");
        return "Update request submitted successfully. An admin will review your request.";
    }

    /**
     * DELETE: A logic method that deletes a Driver from the database using their
     * identification number
     * @param driverId
     *  The identification number of the Driver being deleted
     */
    public void deleteDriver(Integer driverId) {
        //Validation check
        findById(driverId);
        driverRepository.deleteById(driverId);
    }

    /**
     * DELETE: A logic method that deletes all Drivers from the database
     */
    public void deleteAllDrivers() {driverRepository.deleteAll();}

    /**
     * VERIFY: A logic method to verify if the incoming username and password match the
     * username and password stored for the Driver
     * @param loginAttempt
     *  The DTO containing the username and password
     * @return
     *  A LoginResponse with a real JWT token on success, error message on failure
     */
    public LoginResponse driverLogin(LoginRequest loginAttempt) {
        Optional<Driver> driverOpt = driverRepository.findByUserName(loginAttempt.getUserName());

        if (driverOpt.isEmpty()) {
            return new LoginResponse("User not found", null, null, null, null, null);
        }

        Driver driver = driverOpt.get();

        if (!passwordEncoder.matches(loginAttempt.getPassword(), driver.getPassword())) {
            return new LoginResponse("Invalid password", null, null, null, null, null);
        }

        Session session = sessionService.createSession(
                driver.getEmployeeId(),
                driver.getAccessLevel(),
                driver.getUserName()
        );

        String token = jwtUtil.generateToken(session.getSessionId());

        return new LoginResponse(
                "Login successful",
                driver.getEmployeeId(),
                driver.getFirstName(),
                driver.getLastName(),
                driver.getAccessLevel().name(),
                token
        );
    }

    /**
     * LOGOUT: Accepts the raw Authorization header, extracts the session ID from
     * the JWT, and deletes the session row, which immediately invalidating the token.
     * @param authHeader
     *  The raw Authorization header value (e.g. "Bearer &lt;token&gt;")
     * @return
     *  Confirmation message on success, error message if header is missing
     */
    public String driverLogout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "No active session to log out of.";
        }
        String token = authHeader.substring(7);
        String sessionId = jwtUtil.getSessionId(token);
        sessionService.closeSession(sessionId);
        return "Logged out successfully.";
    }
}