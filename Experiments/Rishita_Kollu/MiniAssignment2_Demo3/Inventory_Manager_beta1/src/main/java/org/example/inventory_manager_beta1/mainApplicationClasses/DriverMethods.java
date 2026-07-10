package org.example.inventory_manager_beta1.mainApplicationClasses;

import org.example.inventory_manager_beta1.DTO.Employees.Login.LoginRequest;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.SignUpRequest;
import org.example.inventory_manager_beta1.DTO.Employees.Update.UpdateRequest;
import org.example.inventory_manager_beta1.entities.*;
import org.example.inventory_manager_beta1.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class DriverMethods {
    @Autowired
    private DriverService driverService;

    /**
     * CREATE: A logic method for adding a new Driver
     * @param driverSignUp
     *  The Driver DTO containing the Driver information from a JSON
     * @return
     *  A message to the driver confirming a successful sign-up
     *  and telling them their employeeId
     */
    @PostMapping("/driver/signup")
    public String addDriver (@RequestBody SignUpRequest driverSignUp) {return driverService.registerDriver(driverSignUp).getMessage();}

    /**
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/driver/login")
    public String driverLogin (@RequestBody LoginRequest loginRequest) {return driverService.driverLogin(loginRequest).getMessage();}

    /**
     * READ: A search method that returns all existing drivers
     * @return
     *  A list of all existing drivers
     */
    @GetMapping("/drivers")
    public List<Driver> getDrivers() {return driverService.findAll();}

    /**
     * READ: A search method that returns a specific Driver
     * @param driverSSN
     *  The ID of the Driver
     * @return
     *  The specific Driver
     */
    @GetMapping("/driver/ssn/{driverSSN}")
    public Optional<Driver> getDriver(@PathVariable String driverSSN) {return driverService.findBySSN(driverSSN);}

    /**
     *
     * @param userName
     * @return
     */
    @GetMapping("/driver/username/{userName}")
    public Optional<Driver> getDriverByUsername(@PathVariable String userName) {return driverService.findByUserName(userName);}

    /**
     * UPDATE: A logic method for updating an existing Driver
     * @param driverUpdateRequest
     *  The Driver DTO containing the new Driver information from a JSON
     * @return
     *  True if completed
     */
    @PutMapping("/driver/update")
    public Boolean updateDriver(@RequestBody UpdateRequest driverUpdateRequest) {
        driverService.updateDriver(driverUpdateRequest);
        return true;
    }

    /**
     * DELETE: A logic method that removes a driver from the database based on their driverId
     * @param driverSSN
     *  The ID of the Driver being removed
     * @return
     *  True or False
     */
    @DeleteMapping("/driver/{driverSSN}/delete")
    public Boolean deleteDriver (@PathVariable String driverSSN) {
        Optional<Driver> driver = driverService.findBySSN(driverSSN);
        if (driver.isPresent()) {
            driverService.deleteDriver(driver.get().getEmployeeId());
            return true;
        }
        return false;
    }

    /**
     * DELETE: A logic method that removes all drivers
     * @return
     *  True if completed
     */
    @DeleteMapping("/driver/deleteAll")
    public Boolean deleteAllDrivers() {
        for (Driver driver : driverService.findAll()) {
            driverService.deleteDriver(driver.getEmployeeId());
        }
        return true;
    }
}
