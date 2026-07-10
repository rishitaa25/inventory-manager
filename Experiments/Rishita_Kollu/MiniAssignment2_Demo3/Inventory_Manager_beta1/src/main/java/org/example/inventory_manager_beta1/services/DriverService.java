package org.example.inventory_manager_beta1.services;

import org.example.inventory_manager_beta1.DTO.Employees.Login.LoginRequest;
import org.example.inventory_manager_beta1.DTO.Employees.Login.LoginResponse;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.SignUpRequest;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.SignUpResponse;
import org.example.inventory_manager_beta1.DTO.Employees.Update.UpdateRequest;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.DriverNotFoundException;
import org.example.inventory_manager_beta1.entities.Driver;
import org.example.inventory_manager_beta1.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService {
    @Autowired
    private final DriverRepository driverRepository;

    /**
     * INIT:
     * @param driverRepository
     */
    public DriverService(DriverRepository driverRepository) {this.driverRepository = driverRepository;}

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*==================================SEARCH METHODS==================================*/

    /**
     *
     * @param ssn
     * @return
     */
    public Optional<Driver> findBySSN(String ssn) {return driverRepository.findBySsn(ssn);}

    /**
     *
     * @param driverId
     * @return
     */
    public Driver findByDriverId(Integer driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new DriverNotFoundException(STR."Driver with ID \{driverId} not found"));
    }

    /**
     *
     * @return
     */
    public List<Driver> findAll() {return driverRepository.findAll();}

    /**
     *
     * @param driverIds
     * @return
     */
    public List<Driver> findAllByDriverId(List<Integer> driverIds) {return driverRepository.findAllById(driverIds);}

    /**
     *
     * @param firstName
     * @return
     */
    public List<Driver> findByFirstName(String firstName) {return driverRepository.findByFirstName(firstName);}

    /**
     *
     * @param lastName
     * @return
     */
    public List<Driver> findByLastName(String lastName) {return driverRepository.findByLastName(lastName);}

    /**
     *
     * @param userName
     * @return
     */
    public Optional<Driver> findByUserName(String userName) {return driverRepository.findByUserName(userName);}

    /**
     *
     * @param email
     * @return
     */
    public Optional<Driver> findByEmail(String email) {return driverRepository.findByEmail(email);}

    /**
     *
     * @param phoneNumber
     * @return
     */
    public Optional<Driver> findByPhoneNumber(String phoneNumber) {return driverRepository.findByPhoneNumber(phoneNumber);}

    /*==================================LOGIC METHODS==================================*/

    /**
     *
     * @param driverSignUp
     * @return
     */
    public SignUpResponse registerDriver(SignUpRequest driverSignUp) {
        Driver driver = new Driver(
                driverSignUp.getSsn(),
                driverSignUp.getFirstName(),
                driverSignUp.getLastName(),
                driverSignUp.getUserName(),
                driverSignUp.getEmail(),
                driverSignUp.getPhoneNumber()
        );
        driver.setPassword(passwordEncoder.encode(driverSignUp.getPassword()));
        driver.setAccessLevel(AccessLevel.DRIVER);
        driverRepository.save(driver);
        return new SignUpResponse(driver.getEmployeeId(), driver.getFirstName(), driver.getAccessLevel());
    }

    /**
     *
     * @param driver
     */
    public void saveDriver(Driver driver) {
        driverRepository.save(driver);
    }

    /**
     *
     * @param updateRequest
     */
    public void updateDriver(UpdateRequest updateRequest) {
        Driver updateDriver = driverRepository.findById(updateRequest.getEmployeeId())
                .orElseThrow(() -> new DriverNotFoundException(STR."Driver with ID: \{updateRequest.getEmployeeId()} not found"));
        updateDriver.setFirstName(updateRequest.getFirstName());
        updateDriver.setLastName(updateRequest.getLastName());
        updateDriver.setUserName(updateRequest.getUserName());
        updateDriver.setEmail(updateRequest.getEmail());
        updateDriver.setPhoneNumber(updateRequest.getPhoneNumber());
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isBlank()) {
            updateDriver.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        driverRepository.save(updateDriver);
    }

    /**
     *
     * @param driverId
     */
    public void deleteDriver(Integer driverId) {
        driverRepository.deleteById(driverId);
    }

    /**
     *
     * @param loginAttempt
     * @return
     */
    public LoginResponse driverLogin(LoginRequest loginAttempt) {
        Optional<Driver> driver = driverRepository.findByUserName(loginAttempt.getUserName());
        if (driver.isPresent()) {
            if (!passwordEncoder.matches(loginAttempt.getPassword(), driver.get().getPassword())) {
                return new LoginResponse("Incorrect password. Please try again");
            }
            return new LoginResponse(STR."Successfully logged in as \{loginAttempt.getUserName()}");
        }
        return new LoginResponse("Incorrect username. Please try again");
    }
}