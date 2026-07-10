package org.example.inventory_manager_beta1.services;

import org.example.inventory_manager_beta1.DTO.Employees.Login.LoginRequest;
import org.example.inventory_manager_beta1.DTO.Employees.Login.LoginResponse;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.SignUpRequest;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.SignUpResponse;
import org.example.inventory_manager_beta1.DTO.Employees.Update.UpdateRequest;
import org.example.inventory_manager_beta1.DTO.Employees.Update.UpdateResponse;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.DataEnums.ManagementTitle;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.AdminNotFoundException;
import org.example.inventory_manager_beta1.entities.Admin;
import org.example.inventory_manager_beta1.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
//Class to handle the logic of the Admin object
public class AdminService {
    @Autowired
    //Used for custom search methods
    private final AdminRepository adminRepository;

    /**
     * INIT: Used for initializing the adminRepository for custom database searching
     * @param adminRepository
     *  The repository with custom search methods
     */
    public AdminService(AdminRepository adminRepository) {this.adminRepository = adminRepository;}

    @Autowired
    //Used for password hashing
    private PasswordEncoder passwordEncoder;

    /*==================================SEARCH METHODS==================================*/

    public Admin findById(Integer id) {
        return adminRepository.findById(id)
            .orElseThrow(() -> new AdminNotFoundException(STR."Admin with ID: \{id} not found"));
    }

    /**
     *
     * @param ssn
     * @return
     */
    public Optional<Admin> findBySSN(String ssn){return adminRepository.findBySsn(ssn);}

    /**
     *
     * @param employeeId
     * @return
     */
    public Admin findByAdminId(Integer employeeId) {
        return adminRepository.findById(employeeId)
                .orElseThrow(() -> new AdminNotFoundException(STR."Admin with ID: \{employeeId} not found"));
    }

    /**
     *
     * @param firstName
     * @return
     */
    public List<Admin> findByFirstName(String firstName) {return adminRepository.findByFirstName(firstName);}

    /**
     *
     * @param lastName
     * @return
     */
    public List<Admin> findByLastName(String lastName) {return adminRepository.findByLastName(lastName);}

    /**
     *
     * @param userName
     * @return
     */
    public Admin findByUserName(String userName) {
        return adminRepository.findByUserName(userName)
                .orElseThrow(() -> new AdminNotFoundException("Admin with username \"" + userName + "\" not found"));
    }

    /**
     *
     * @param email
     * @return
     */
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminNotFoundException("Admin with email \"" + email + "\" not found"));
    }

    /**
     *
     * @param phoneNumber
     * @return
     */
    public Admin findByPhoneNumber(String phoneNumber) {
        return adminRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AdminNotFoundException("Admin with phone number \"" + phoneNumber + "\" not found"));
    }

    /**
     *
     * @param managementTitle
     * @return
     */
    public List<Admin> findByManagementTitle(ManagementTitle managementTitle) {return adminRepository.findByManagementTitle(managementTitle);}

    /**
     *
     * @return
     */
    public List<Admin> findAll(){return adminRepository.findAll();}

    /*==================================LOGIC METHODS==================================*/

    /**
     * UPDATE:
     * @param admin
     */
    public void saveAdmin(Admin admin) {adminRepository.save(admin);}

    /**
     * CREATE:
     * @param adminSignUp
     */
    public SignUpResponse registerAdmin(SignUpRequest adminSignUp){
        Admin newAdmin = new Admin(adminSignUp.getSsn(),
                                   adminSignUp.getFirstName(),
                                   adminSignUp.getLastName(),
                                   adminSignUp.getUserName(),
                                   adminSignUp.getEmail(),
                                   adminSignUp.getPhoneNumber());
        newAdmin.setPassword(passwordEncoder.encode(adminSignUp.getPassword())); //Backend password hashing
        newAdmin.setAccessLevel(AccessLevel.ADMIN);
        adminRepository.save(newAdmin); //Save new Admin to database
        return new SignUpResponse(newAdmin.getEmployeeId(), newAdmin.getFirstName(), newAdmin.getAccessLevel());
    }

    /**
     *
     * @param adminUpdateRequest
     * @return
     */
    public UpdateResponse updateAdmin(UpdateRequest adminUpdateRequest) {
        Optional<Admin> admin = adminRepository.findById(adminUpdateRequest.getEmployeeId());
        if (admin.isPresent()) {
            Admin updatedAdmin = admin.get();
            updatedAdmin.setUserName(adminUpdateRequest.getUserName());
            updatedAdmin.setFirstName(adminUpdateRequest.getFirstName());
            updatedAdmin.setLastName(adminUpdateRequest.getLastName());
            updatedAdmin.setEmail(adminUpdateRequest.getEmail());
            updatedAdmin.setPhoneNumber(adminUpdateRequest.getPhoneNumber());
            if (adminUpdateRequest.getPassword() != null && !adminUpdateRequest.getPassword().isBlank()) {
                updatedAdmin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
            }
            saveAdmin(updatedAdmin);
            return new UpdateResponse(adminUpdateRequest.getEmployeeId(), adminUpdateRequest.getUserName());
        }
        throw new AdminNotFoundException(STR."Admin with ID:\{adminUpdateRequest.getEmployeeId()} not found");
    }

    /**
     * DELETE
     * @param adminSSN
     */
    public void deleteAdmin(String adminSSN){
        Admin admin = findBySSN(adminSSN)
                .orElseThrow(() -> new AdminNotFoundException(STR."Admin with SSN: \{adminSSN} not found"));
        adminRepository.deleteById(admin.getEmployeeId());
    }

    /**
     *
     * @param loginAttempt
     * @return
     *
     */
    public LoginResponse adminLogin(LoginRequest loginAttempt) {
        Optional<Admin> admin = adminRepository.findByUserName(loginAttempt.getUserName());
        if (admin.isPresent()) {
            if (!passwordEncoder.matches(loginAttempt.getPassword(), admin.get().getPassword())) {
                return new LoginResponse("Incorrect password. Please try again");
            }
            return new LoginResponse(STR."Successfully logged in as \{loginAttempt.getUserName()}");
        }
        return new LoginResponse("Incorrect username. Please try again");
    }
}
