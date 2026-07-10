package org.example.inventory_manager_beta1.mainApplicationClasses;

import org.example.inventory_manager_beta1.DTO.Employees.Login.*;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.*;
import org.example.inventory_manager_beta1.DTO.Employees.Update.*;
import org.example.inventory_manager_beta1.entities.Employee;
import org.example.inventory_manager_beta1.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class EmployeeMethods {
    @Autowired
    private EmployeeService employeeService;

    /**
     * CREATE: A controller method that handles creating a new Employee
     * @param signUpRequest
     *  The DTO containing the Employee information in JSON format
     * @return
     *  A message confirming if the sign-up is successful
     */
    @PostMapping("/employee/signup")
    public String employeeSignup (@RequestBody SignUpRequest signUpRequest) {return employeeService.registerEmployee(signUpRequest).getMessage();}

    /**
     * LOGIN: A controller method that handles logging in an existing Employee
     * @param loginAttempt
     *  The DTO containing the Employee's username and password
     * @return
     *  A message containing details about the login attempt
     *  See the EmployeeService.login method for more details
     */
    @PostMapping("/employee/login")
    public String login(@RequestBody LoginRequest loginAttempt) {return employeeService.employeeLogin(loginAttempt).getMessage();}

    /**
     * READ: A controller method that handles getting all existing Employees
     * @return
     *  A list containing all current Employees
     */
    @GetMapping("/employees")
    public List<Employee> employees() {return employeeService.findAll();}

    /**
     * READ: A controller method that handles getting a specific Employee
     * @param employeeSSN
     *  The SSN of the Employee
     * @return
     *  The Employee with the matching SSN if they exist
     */
    @GetMapping("/employee/ssn/{employeeSSN}")
    public Employee getEmployeeSSN(@PathVariable("employeeSSN") String employeeSSN) {return employeeService.findBySSN(employeeSSN);}

    /**
     * READ: A controller method that handles getting a specific Employee
     * @param userName
     *  The username of the Employee
     * @return
     *  The Employee with the matching username if they exist
     */
    @GetMapping("/employee/username/{userName}")
    public Employee getEmployeeUsername(@PathVariable String userName) {return employeeService.findByUserName(userName);}

    /**
     * UPDATE: A controller method that handles updating an existing Employee
     * @param employeeUpdateRequest
     *  The DTO containing the updated Employee information
     * @return
     *  A message confirming a successful information update
     */
    @PutMapping("/employee/update")
    public String updateEmployee(@RequestBody UpdateRequest employeeUpdateRequest) {return employeeService.updateEmployee(employeeUpdateRequest).getMessage();}

    /**
     * DELETE: A controller method that handles deleting a specific Employee using SSN
     * @param employeeSSN
     *  The SSN of the Employee
     * @return
     *  True if completed
     */
    @DeleteMapping("/delete/employee/{employeeSSN}")
    public Boolean deleteEmployee(@PathVariable String employeeSSN) {
        employeeService.deleteEmployee(employeeSSN);
        return true;
    }

    /**
     * DELETE: A controller method that handles deleting all employees
     * @return
     *  True if completed
     */
    @DeleteMapping("/employee/deleteAll")
    public Boolean deleteAllEmployees() {
        for (Employee employee : employeeService.findAll()) {
            employeeService.deleteEmployee(employee.getSsn());
        }
        return true;
    }
}
