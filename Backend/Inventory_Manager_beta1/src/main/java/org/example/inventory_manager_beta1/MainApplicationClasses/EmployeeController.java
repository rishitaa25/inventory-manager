package org.example.inventory_manager_beta1.MainApplicationClasses;

import org.example.inventory_manager_beta1.Configuration.Annotation.RequiresAccess;
import org.example.inventory_manager_beta1.DTO.Employees.Login.*;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.*;
import org.example.inventory_manager_beta1.DTO.Employees.Update.*;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.Entities.Employee;
import org.example.inventory_manager_beta1.Entities.Shift;
import org.example.inventory_manager_beta1.Services.EmployeeService;
import org.example.inventory_manager_beta1.Services.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * A controller class containing all the different HTTP mappings for Employee related methods
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ShiftService shiftService;

    /**
     * CREATE: A controller method for handling Employee creation
     * @param signUpRequest
     *  The DTO containing the Employee information in JSON format
     * @return
     *  A message confirming if the sign-up is successful
     */
    @PostMapping("/signup")
    public SignUpResponse employeeSignup (@RequestBody SignUpRequest signUpRequest) {return employeeService.registerEmployee(signUpRequest);}

    /**
     * VERIFY: A controller method to handle verifying an Employee's login attempt
     * @param loginRequest
     *  The DTO containing the Employee's username and password
     * @return
     *  A LoginResponse containing the session token on success
     */
    @PostMapping("/login")
    public LoginResponse login (@RequestBody LoginRequest loginRequest) {return employeeService.employeeLogin(loginRequest);}

    /**
     * LOGOUT: A controller method to handle closing a session opened by an Employee
     * @param authHeader
     *  The Authorization header from the request
     * @return
     *  Confirmation message on success, error message if no token provided
     */
    @PostMapping("/logout")
    public ResponseEntity<String> employeeLogout (@RequestHeader(value = "Authorization", required = false) String authHeader) {return ResponseEntity.ok(employeeService.employeeLogout(authHeader));}

    /**
     * READ: A controller method for handling searching for a specific Employee
     * @param employeeId
     *  The EmployeeID of the Employee
     * @return
     *  The Employee with the matching ID if they exist
     */
    @RequiresAccess(AccessLevel.EMPLOYEE)
    @GetMapping("/find-by/id/{employeeId}")
    public Employee getEmployeeViaId(@PathVariable("employeeId") Integer employeeId) {return employeeService.findById(employeeId);}

    /**
     * READ: A controller method for handling searching for a specific Employee
     * @param employeeSSN
     *  The SSN of the specific Employee
     * @return
     *  The Employee with the matching SSN if they exist
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("find-by/ssn/{employeeSSN}")
    public Employee getEmployeeViaSSN(@PathVariable("employeeSSN") String employeeSSN) {return employeeService.findBySSN(employeeSSN);}

    /**
     * READ: A controller method for handling searching for a specific Employee
     * @param employeeUserName
     *  The username of the Employee
     * @return
     *  The Employee with the matching username if they exist
     */
    @RequiresAccess(AccessLevel.EMPLOYEE)
    @GetMapping("/find-by/username/{employeeUserName}")
    public Employee getEmployeeViaUserName(@PathVariable String employeeUserName) {return employeeService.findByUserName(employeeUserName);}

    /**
     * READ: A controller method for handling searching for a specific Employee
     * @param employeeFirstName
     *  The first name of the specific Employee
     * @return
     *  A List of Employee(s) with a matching first name
     */
    @RequiresAccess(AccessLevel.EMPLOYEE)
    @GetMapping("find-by/first-name/{employeeFirstName}")
    public List<Employee> getEmployeeViaFirstName(@PathVariable("employeeFirstName") String employeeFirstName) {return employeeService.findByFirstName(employeeFirstName);}

    /**
     * READ: A controller method for handling searching for a specific Employee
     * @param employeeLastName
     *  The last name of the specific Employee
     * @return
     *  A List of Employee(s) with a matching last name
     */
    @RequiresAccess(AccessLevel.EMPLOYEE)
    @GetMapping("find-by/last-name/{employeeLastName}")
    public List<Employee> getEmployeeViaLastName(@PathVariable("employeeLastName") String employeeLastName) {return employeeService.findByLastName(employeeLastName);}

    /**
     * READ: A controller method for handling searching for a specific Employee
     * @param employeeEmail
     *  The email of the specific Employee
     * @return
     *  The Employee with the matching email if they exist
     */
    @RequiresAccess(AccessLevel.EMPLOYEE)
    @GetMapping("find-by/email/{employeeEmail}")
    public Employee getEmployeeViaEmail(@PathVariable("employeeEmail") String employeeEmail) {return employeeService.findByEmail(employeeEmail);}

    /**
     * READ: A controller method for handling searching for a specific Employee
     * @param employeePhoneNumber
     *  The phone number of the specific Employee
     * @return
     *  The Employee with the matching phone number if they exist
     */
    @RequiresAccess(AccessLevel.EMPLOYEE)
    @GetMapping("find-by/phone-number/{employeePhoneNumber}")
    public Employee getEmployeeViaPhoneNumber(@PathVariable("employeePhoneNumber") String employeePhoneNumber) {return employeeService.findByPhoneNumber(employeePhoneNumber);}

    /**
     * READ: A controller method for handling searching for a specific Employee
     * @param employeeAccessLevel
     *  The access level of the specific Employee
     * @return
     *  A List of Employee(s) with a matching access level
     */
    @RequiresAccess(AccessLevel.EMPLOYEE)
    @GetMapping("find-by/access-level/{employeeAccessLevel}")
    public List<Employee> getEmployeeViaAccessLevel(@PathVariable("employeeAccessLevel") AccessLevel employeeAccessLevel) {return employeeService.findByAccessLevel(employeeAccessLevel);}

    /**
     * READ: A controller method for handling searching for all Employee
     * @return
     *  A list containing all current Employees
     */
    @RequiresAccess(AccessLevel.EMPLOYEE)
    @GetMapping("/find/all")
    public List<Employee> employees() {return employeeService.findAll();}

    /**
     * READ: A controller method that handles getting all shifts for a specific Employee
     * @param employeeId
     *  The ID of the Employee
     * @return
     *  A list of shifts for the matching Employee
     */
    @RequiresAccess(AccessLevel.EMPLOYEE)
    @GetMapping("/{employeeId}/all-shifts")
    public List<Shift> getShifts(@PathVariable("employeeId") Integer employeeId) {return shiftService.findAllEmployeeShifts(employeeId);}

    /**
     * READ: A controller method that handles getting weekly shifts for a specific Employee
     * @param employeeId
     *  The ID of the Employee
     * @param startDay
     *  The start date of the week
     * @return
     *  A list of shifts for the matching Employee within the week
     */
    @RequiresAccess(AccessLevel.EMPLOYEE)
    @GetMapping("/{employeeId}/weekly-shifts/{startDay}")
    public List<Shift> getWeeklyShifts(@PathVariable("employeeId") Integer employeeId, @PathVariable("startDay") LocalDate startDay) {return shiftService.findWeeklyEmployeeShifts(employeeId, startDay);}

    /**
     * REQUEST: Submits an update request for admin approval rather than applying
     * changes directly. The request is stored as a PENDING notification sent to admins.
     * @param employeeUpdateRequest
     *  The DTO containing the updated Employee information
     * @return
     *  A message confirming the request was submitted for review
     */
    @RequiresAccess(AccessLevel.EMPLOYEE)
    @PutMapping("/update")
    public String updateEmployee(@RequestBody UpdateEmployeeRequest employeeUpdateRequest) {return employeeService.requestUpdate(employeeUpdateRequest);}

    /**
     * DELETE: A controller method for handling specific Employee deletion
     * @param employeeId
     *  The ID of the Employee
     * @return
     *  True if completed
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @DeleteMapping("/delete/{employeeId}")
    public Boolean deleteEmployee(@PathVariable("employeeId") Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
        return true;
    }

    /**
     * DELETE: A controller method for handling removing all Employees
     * @return
     *  True if completed
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @DeleteMapping("/delete/all")
    public Boolean deleteAllEmployees() {
        employeeService.deleteAllEmployees();
        return true;
    }
}
