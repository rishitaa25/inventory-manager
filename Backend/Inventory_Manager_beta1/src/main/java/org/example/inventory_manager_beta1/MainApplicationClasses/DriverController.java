package org.example.inventory_manager_beta1.MainApplicationClasses;

import org.example.inventory_manager_beta1.Configuration.Annotation.RequiresAccess;
import org.example.inventory_manager_beta1.DTO.Employees.Login.LoginRequest;
import org.example.inventory_manager_beta1.DTO.Employees.Login.LoginResponse;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.SignUpRequest;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.SignUpResponse;
import org.example.inventory_manager_beta1.DTO.Employees.Update.UpdateEmployeeRequest;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.Entities.*;
import org.example.inventory_manager_beta1.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * A controller class containing all the different HTTP mappings for Driver related methods
 */
@RestController
@RequestMapping("/driver")
public class DriverController {
    /**
     * The auto-injected driverService class for handling Driver related logic
     */
    @Autowired
    private DriverService driverService;
    /**
     * The auto-injected ShiftService class for handling Shift related logic
     */
    @Autowired
    private ShiftService shiftService;

    /**
     * CREATE: A controller method for handling Driver creation
     * @param driverSignUp
     *  The DTO containing the Driver information from a JSON
     * @return
     *  A message confirming if the sign-up is successful
     */
    @PostMapping("/signup")
    public SignUpResponse addDriver (@RequestBody SignUpRequest driverSignUp) {return driverService.registerDriver(driverSignUp);}

    /**
     * VERIFY: A controller method to handle verifying a Driver's login attempt
     * @param loginRequest
     *  The DTO containing the Driver's username and password
     * @return
     *  A message containing details about the login attempt
     */
    @PostMapping("/login")
    public LoginResponse driverLogin (@RequestBody LoginRequest loginRequest) {return driverService.driverLogin(loginRequest);}

    /**
     * LOGOUT: A controller method to handle closing a session a Driver opened
     * @param authHeader
     *  The Authorization header from the request
     * @return
     *  Confirmation message on success, error message if no token provided
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout (@RequestHeader(value = "Authorization", required = false) String authHeader) {return ResponseEntity.ok(driverService.driverLogout(authHeader));}

    /**
     * READ: A controller method for handling searching a specific Driver
     * @param driverId
     *  The EmployeeID of the Driver
     * @return
     *  The specific Driver
     */
    @RequiresAccess(AccessLevel.DRIVER)
    @GetMapping("/find-by/id/{driverId}")
    public Driver getDriverViaId (@PathVariable("driverId") Integer driverId) {return driverService.findById(driverId);}

    /**
     * READ: A controller method for handling searching for a specific Driver
     * @param driverSSN
     *  The SSN of the specific Driver
     * @return
     *  The Driver with the matching SSN if they exist
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("find-by/ssn/{driverSSN}")
    public Driver getDriverViaSSN (@PathVariable("driverSSN") String driverSSN) {return driverService.findBySSN(driverSSN);}

    /**
     * READ: A controller method for handling searching for a specific Driver
     * @param driverFirstName
     *  The first name of the specific Driver
     * @return
     *  A List of Driver(s) with a matching first name
     */
    @RequiresAccess(AccessLevel.DRIVER)
    @GetMapping("find-by/first-name/{driverFirstName}")
    public List<Driver> getDriverViaFirstName (@PathVariable("driverFirstName") String driverFirstName) {return driverService.findByFirstName(driverFirstName);}

    /**
     * READ: A controller method for handling searching for a specific Driver
     * @param driverLastName
     *  The last name of the specific Driver
     * @return
     *  A List of Driver(s) with a matching last name
     */
    @RequiresAccess(AccessLevel.DRIVER)
    @GetMapping("find-by/last-name/{driverLastName}")
    public List<Driver> getDriverViaLastName (@PathVariable("driverLastName") String driverLastName) {return driverService.findByLastName(driverLastName);}

    /**
     * READ: A controller method for handling searching for a specific Driver
     * @param driverEmail
     *  The email of the specific Driver
     * @return
     *  The Driver with the matching email if they exist
     */
    @RequiresAccess(AccessLevel.DRIVER)
    @GetMapping("find-by/email/{driverEmail}")
    public Driver getDriverViaEmail (@PathVariable("driverEmail") String driverEmail) {return driverService.findByEmail(driverEmail);}

    /**
     * READ: A controller method for handling searching for a specific Driver
     * @param driverPhoneNumber
     *  The phone number of the specific Driver
     * @return
     *  The Driver with the matching phone number if they exist
     */
    @RequiresAccess(AccessLevel.DRIVER)
    @GetMapping("find-by/phone-number/{driverPhoneNumber}")
    public Driver getDriverViaPhoneNumber (@PathVariable("driverPhoneNumber") String driverPhoneNumber) {return driverService.findByPhoneNumber(driverPhoneNumber);}

    /**
     * READ: A controller method for handling searching a specific Driver
     * @param driverUserName
     *  The username of the Driver
     * @return
     *  The specific Driver
     */
    @RequiresAccess(AccessLevel.DRIVER)
    @GetMapping("/find-by/username/{driverUserName}")
    public Driver getDriverViaUserName (@PathVariable String driverUserName) {return driverService.findByUserName(driverUserName);}

    /**
     * READ: A controller method for handling searching for all Drivers
     * @return
     *  A list of all existing drivers
     */
    @RequiresAccess(AccessLevel.DRIVER)
    @GetMapping("/find/all")
    public List<Driver> getDrivers () {return driverService.findAll();}

    /**
     * READ: A controller method that handles getting all shifts for a specific Driver
     * @param driverId
     *  The ID of the Driver
     * @return
     *  A list of shifts for the matching Driver
     */
    @RequiresAccess(AccessLevel.DRIVER)
    @GetMapping("/{driverId}/all-shifts")
    public List<Shift> getShifts (@PathVariable("driverId") Integer driverId) {return shiftService.findAllEmployeeShifts(driverId);}

    /**
     * READ: A controller method that handles getting weekly shifts for a specific Driver
     * @param driverId
     *  The ID of the Driver
     * @param startDay
     *  The start date of the week
     * @return
     *  A list of shifts for the matching Driver within the week
     */
    @RequiresAccess(AccessLevel.DRIVER)
    @GetMapping("/{driverId}/weekly-shifts/{startDay}")
    public List<Shift> getWeeklyShifts (@PathVariable("driverId") Integer driverId, @PathVariable("startDay") LocalDate startDay) {return shiftService.findWeeklyEmployeeShifts(driverId, startDay);}

    /**
     * UPDATE: A controller method for handling updating an existing Driver
     * @param driverUpdateRequest
     *  The DTO containing the new Driver information from a JSON
     * @return
     *  A message confirming a successful information update
     */
    @RequiresAccess(AccessLevel.DRIVER)
    @PutMapping("/update")
    public String updateDriver (@RequestBody UpdateEmployeeRequest driverUpdateRequest) {return driverService.requestUpdate(driverUpdateRequest);}

    /**
     * DELETE: A controller method for handling specific Driver deletion
     * @param driverId
     *  The ID of the Driver being removed
     * @return
     *  True if completed
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @DeleteMapping("/delete/{driverId}")
    public Boolean deleteDriver (@PathVariable Integer driverId) {
        driverService.deleteDriver(driverId);
        return true;
    }

    /**
     * DELETE: A controller method for handling removing all drivers
     * @return
     *  True if completed
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @DeleteMapping("/delete/all")
    public Boolean deleteAllDrivers () {
        driverService.deleteAllDrivers();
        return true;
    }
}
