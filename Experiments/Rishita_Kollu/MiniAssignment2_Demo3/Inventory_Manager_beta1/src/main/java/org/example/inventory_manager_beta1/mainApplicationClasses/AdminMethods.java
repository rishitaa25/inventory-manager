package org.example.inventory_manager_beta1.mainApplicationClasses;

import org.example.inventory_manager_beta1.DTO.Employees.Login.LoginRequest;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.SignUpRequest;
import org.example.inventory_manager_beta1.DTO.Employees.Update.UpdateRequest;
import org.example.inventory_manager_beta1.entities.Admin;
import org.example.inventory_manager_beta1.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminMethods {
    @Autowired
    private AdminService adminService;

    /**
     * CREATE: A logic method for creating an Admin object
     * @param signUpRequest
     *  The Admin DTO containing the Admin information from a JSON
     * @return
     *  A message confirming if the sign-up is successful
     */
    @PostMapping("/admin/signup")
    public String adminSignup (@RequestBody SignUpRequest signUpRequest) {return adminService.registerAdmin(signUpRequest).getMessage();}

    /**
     * READ: A search method for getting all admins
     * @return
     *  The list of current admins
     */
    @GetMapping("/admins")
    public List<Admin> admins() {return adminService.findAll();}

    /**
     * READ: A search method for getting a specific Admin
     * @param adminSSN
     *  The SSN of the specific Admin
     * @return
     *  The specific Admin
     */
    @GetMapping("/admin/{adminSSN}")
    public Optional<Admin> findSpecificAdmin(@PathVariable String adminSSN) {return adminService.findBySSN(adminSSN);}

    /**
     * UPDATE: A logic method for updating an existing Admin object
     * @param updateAdmin
     *  The SSN of the admin being updated
     * @return
     *  True or False
     */
    @PutMapping("/admin/update")
    public Boolean updateAdmin(@RequestBody UpdateRequest updateAdmin) {
        adminService.updateAdmin(updateAdmin);
        return true;
    }

    /**
     * DELETE: A logic method for deleting a specific Admin from the database
     * @param adminSSN
     *  The ssn of the Admin
     * @return
     *  True or False
     */
    @DeleteMapping("/delete/admin/{adminSSN}")
    public Boolean deleteAdmin(@PathVariable String adminSSN) {
        adminService.deleteAdmin(adminSSN);
        return true;
    }

    /**
     * DELETE: A logic method for deleting all admins
     * @return
     *  True if completed
     */
    @DeleteMapping("/admin/deleteAll")
    public Boolean deleteAllAdmins() {
         for (Admin admin : adminService.findAll()) {
             adminService.deleteAdmin(admin.getSsn());
         }
         return true;
    }

    /**
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/admin/login")
    public String adminLogin(@RequestBody LoginRequest loginRequest) {return adminService.adminLogin(loginRequest).getMessage();}
}
