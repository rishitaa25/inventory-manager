package org.example.inventory_manager_beta1.Services;

import org.example.inventory_manager_beta1.Configuration.SessionToken.JwtUtil;
import org.example.inventory_manager_beta1.DTO.Employees.Login.*;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.*;
import org.example.inventory_manager_beta1.DTO.Employees.Update.*;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.DataEnums.ManagementTitle;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.AdminNotFoundException;
import org.example.inventory_manager_beta1.Entities.Admin;
import org.example.inventory_manager_beta1.Entities.Session;
import org.example.inventory_manager_beta1.Repositories.AdminRepository;
import org.example.inventory_manager_beta1.Repositories.DriverRepository;
import org.example.inventory_manager_beta1.Repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * A robust service class to handle all the different logic and search methods relating
 * to the Admin class. It is complete with all the AdminRepository search methods
 * defined, and it handles every Admin related logic method with included custom error handling
 */
@Service
public class AdminService {

    private final EmployeeRepository employeeRepository;
    private final DriverRepository driverRepository;
    private final AdminRepository adminRepository;
    private final SessionService sessionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AdminService(EmployeeRepository employeeRepository, DriverRepository driverRepository, AdminRepository adminRepository, SessionService sessionService) {
        this.employeeRepository = employeeRepository;
        this.driverRepository = driverRepository;
        this.adminRepository = adminRepository;
        this.sessionService = sessionService;
    }

    /*==================================SEARCH METHODS==================================*/

    /**
     * SEARCH: A search method for finding an Admin using their identification number
     * @param id
     *  The identification number of the Admin being searched for
     * @return
     *  The Admin with the matching ID number if they exist
     * @throws AdminNotFoundException
     *  Custom NotFound exception if the Admin ID search returns null
     */
    public Admin findById(Integer id) {
        return adminRepository.findById(id)
            .orElseThrow(() -> new AdminNotFoundException("Admin with EmployeeID: " + id + " not found"));
    }

    /**
     * SEARCH: A search method for finding an Admin using their Social Security Number
     * @param ssn
     *  The Social Security Number of the Admin being searched for
     * @return
     *  The Admin with the matching SSN if they exist
     * @throws AdminNotFoundException
     *  Custom NotFound exception if the Admin SSN search returns null
     */
    public Admin findBySSN(String ssn){
        return adminRepository.findBySsn(ssn)
                .orElseThrow(() -> new AdminNotFoundException("Admin with SSN: " + ssn + " not found"));
    }

    /**
     * SEARCH: A search method for finding Admin(s) using their first name
     * @param firstName
     *  The first name of the Admin(s) being searched for
     * @return
     *  A list of all Admins with a matching first name
     */
    public List<Admin> findByFirstName(String firstName) {return adminRepository.findByFirstName(firstName);}

    /**
     * SEARCH: A search method for finding Admin(s) using their last name
     * @param lastName
     *  The last name of the Admin(s) being searched for
     * @return
     *  A list of all Admins with a matching last name
     */
    public List<Admin> findByLastName(String lastName) {return adminRepository.findByLastName(lastName);}

    /**
     * SEARCH: A search method for finding an Admin using their username
     * @param userName
     *  The username of the Admin being searched for
     * @return
     *  The Admin with the matching username if they exist
     * @throws AdminNotFoundException
     *  Custom NotFound exception if the Admin username search returns null
     */
    public Admin findByUserName(String userName) {
        return adminRepository.findByUserName(userName)
                .orElseThrow(() -> new AdminNotFoundException("Admin with username: " + userName + " not found"));
    }

    /**
     * SEARCH: A search method for finding an Admin using their email address
     * @param email
     *  The email of the Admin being searched for
     * @return
     *  The Admin with the matching email if they exist
     * @throws AdminNotFoundException
     *  Custom NotFound exception if the Admin email search returns null
     */
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminNotFoundException("Admin with email: " + email + " not found"));
    }

    /**
     * SEARCH: A search method for finding an Admin using their phone number
     * @param phoneNumber
     *  The phone number of the Admin being searched for
     * @return
     *  The Admin with the matching phone number if they exist
     * @throws AdminNotFoundException
     *  Custom NotFound exception if the Admin phone number search returns null
     */
    public Admin findByPhoneNumber(String phoneNumber) {
        return adminRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AdminNotFoundException("Admin with phone number: " + phoneNumber + " not found"));
    }

    /**
     * SEARCH: A search method for finding Admin(s) using their access level
     * @param accessLevel
     *  The access level of the Admin(s) being searched for
     * @return
     *  A list of all Admins with a matching access level
     */
    public List<Admin> findByAccessLevel(AccessLevel accessLevel) {return adminRepository.findByAccessLevel(accessLevel);}

    /**
     * SEARCH: A search method for finding Admin(s) using their management title
     * @param managementTitle
     *  The management title of the Admin(s) being searched for
     * @return
     *  A list of all Admins with a matching management title
     */
    public List<Admin> findByManagementTitle(ManagementTitle managementTitle) {return adminRepository.findByManagementTitle(managementTitle);}

    /**
     * SEARCH: A search method for finding all Admins in the database
     * @return
     *  A list of all existing Admins in the database
     */
    public List<Admin> findAll(){return adminRepository.findAll();}

    /*==================================LOGIC METHODS==================================*/

    /**
     * UPDATE: A logic method that saves an Admin to the database
     * @param admin
     *  The Admin being saved
     */
    public void saveAdmin(Admin admin) {adminRepository.save(admin);}

    /**
     * CREATE: A logic method that creates a new Admin object, automatically
     * hashes their password using Argon2, assigns an access level, and adds it
     * to the database
     * @param adminSignUp
     *  The DTO containing all the Admin's information
     * @return
     *  A DTO containing the new Admin's ID, first name, and access level
     */
    public SignUpResponse registerAdmin(SignUpRequest adminSignUp){
        Admin newAdmin = new Admin(adminSignUp.getSsn(),
                                   adminSignUp.getFirstName(),
                                   adminSignUp.getLastName(),
                                   adminSignUp.getUserName(),
                                   adminSignUp.getEmail(),
                                   adminSignUp.getPhoneNumber(),
                                   adminSignUp.getManagementTitle());
        newAdmin.setPassword(passwordEncoder.encode(adminSignUp.getPassword())); //Backend password hashing
        //Check to make sure the very first person added to the system
        //has access to every function
        if (employeeRepository.count() == 0 && driverRepository.count() == 0 && adminRepository.count() == 0) {
            newAdmin.setAccessLevel(AccessLevel.GENERAL_MANAGER);
        }
        else {
            newAdmin.setAccessLevel(AccessLevel.ADMIN);
        }
        saveAdmin(newAdmin); //Save new Admin to database
        return new SignUpResponse(newAdmin.getEmployeeId(), newAdmin.getFirstName(), newAdmin.getUserName(), newAdmin.getAccessLevel());
    }

    /**
     * UPDATE: A logic method that updates an existing Admin's information
     * @param adminUpdateRequest
     *  The DTO containing the new information for the Admin
     * @return
     *  A DTO containing the updated Admin's ID and username
     */
    public UpdateEmployeeResponse updateAdmin(UpdateEmployeeRequest adminUpdateRequest) {
        Admin updatedAdmin = findById(adminUpdateRequest.getEmployeeId());
        if (adminUpdateRequest.getFirstName() != null)
            updatedAdmin.setFirstName(adminUpdateRequest.getFirstName());
        if (adminUpdateRequest.getLastName() != null)
            updatedAdmin.setLastName(adminUpdateRequest.getLastName());
        if (adminUpdateRequest.getUserName() != null)
            updatedAdmin.setUserName(adminUpdateRequest.getUserName());
        if (adminUpdateRequest.getEmail() != null)
            updatedAdmin.setEmail(adminUpdateRequest.getEmail());
        if (adminUpdateRequest.getPhoneNumber() != null)
            updatedAdmin.setPhoneNumber(adminUpdateRequest.getPhoneNumber());
        if (adminUpdateRequest.getPassword() != null && !adminUpdateRequest.getPassword().isBlank()) {
            updatedAdmin.setPassword(passwordEncoder.encode(adminUpdateRequest.getPassword()));
        }
        saveAdmin(updatedAdmin);
        return new UpdateEmployeeResponse(adminUpdateRequest.getEmployeeId(), adminUpdateRequest.getUserName());
    }

    /**
     * DELETE: A logic method that deletes an Admin from the database using their
     * identification number
     * @param adminId
     *  The identification number of the Admin being deleted
     */
    public void deleteAdmin(Integer adminId){
        //Validation check
        findById(adminId);
        adminRepository.deleteById(adminId);
    }

    /**
     * DELETE: A logic method that deletes all Admins from the database
     */
    public void deleteAllAdmin() {adminRepository.deleteAll();}

    /**
     * VERIFY: A logic method to verify if the incoming username and password match the
     * username and password stored for the Admin
     * @param loginAttempt
     *  The DTO containing the username and password
     * @return
     *  A LoginResponse with a real JWT token on success, error message on failure
     */
    public LoginResponse adminLogin(LoginRequest loginAttempt) {
        Optional<Admin> adminOpt = adminRepository.findByUserName(loginAttempt.getUserName());

        if (adminOpt.isEmpty()) {
            return new LoginResponse("User not found", null, null, null, null, null);
        }

        Admin admin = adminOpt.get();

        if (!passwordEncoder.matches(loginAttempt.getPassword(), admin.getPassword())) {
            return new LoginResponse("Invalid password", null, null, null, null, null);
        }

        Session session = sessionService.createSession(
                admin.getEmployeeId(),
                admin.getAccessLevel(),
                admin.getUserName()
        );

        String token = jwtUtil.generateToken(session.getSessionId());

        return new LoginResponse(
                "Login successful",
                admin.getEmployeeId(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.getAccessLevel().name(),
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
    public String adminLogout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "No active session to log out of.";
        }
        String token = authHeader.substring(7);
        String sessionId = jwtUtil.getSessionId(token);
        sessionService.closeSession(sessionId);
        return "Logged out successfully.";
    }
}
