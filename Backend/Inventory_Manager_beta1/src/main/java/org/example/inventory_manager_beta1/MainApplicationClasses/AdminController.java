package org.example.inventory_manager_beta1.MainApplicationClasses;

import org.example.inventory_manager_beta1.Configuration.Annotation.RequiresAccess;
import org.example.inventory_manager_beta1.DTO.Employees.Login.LoginRequest;
import org.example.inventory_manager_beta1.DTO.Employees.Login.LoginResponse;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.SignUpRequest;
import org.example.inventory_manager_beta1.DTO.Employees.SignUp.SignUpResponse;
import org.example.inventory_manager_beta1.DTO.Employees.Update.UpdateEmployeeRequest;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.DataEnums.ManagementTitle;
import org.example.inventory_manager_beta1.DataEnums.Status;
import org.example.inventory_manager_beta1.Entities.Admin;
import org.example.inventory_manager_beta1.Entities.Notification;
import org.example.inventory_manager_beta1.Entities.Shift;
import org.example.inventory_manager_beta1.Services.AdminService;
import org.example.inventory_manager_beta1.Services.NotificationService;
import org.example.inventory_manager_beta1.Services.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * A controller class containing all the different HTTP mappings for Admin related methods
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private NotificationService notificationService;

    /**
     * CREATE: A controller method for handling Admin creation
     * @param signUpRequest
     *  The DTO containing the Admin information from a JSON
     * @return
     *  A message confirming if the sign-up is successful
     */
    @PostMapping("/signup")
    public SignUpResponse adminSignup (@RequestBody SignUpRequest signUpRequest) {return adminService.registerAdmin(signUpRequest);}

    /**
     * VERIFY: A controller method to handle verifying an Admin's login attempt
     * @param loginRequest
     *  The DTO containing the Admin's username and password
     * @return
     *  A LoginResponse containing the session token on success
     */
    @PostMapping("/login")
    public LoginResponse adminLogin (@RequestBody LoginRequest loginRequest) {return adminService.adminLogin(loginRequest);}

    /**
     * LOGOUT: A controller method to handle closing a session opened by an Admin
     * @param authHeader
     *  The Authorization header from the request
     * @return
     *  Confirmation message on success, error message if no token provided
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout (@RequestHeader(value = "Authorization", required = false) String authHeader) {return ResponseEntity.ok(adminService.adminLogout(authHeader));}

    /**
     * READ: A controller method for handling searching for a specific Admin
     * @param adminId
     *  The EmployeeID of the specific Admin
     * @return
     *  The Admin with the matching ID if they exist
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("find-by/id/{adminId}")
    public Admin getAdminViaId (@PathVariable("adminId") Integer adminId) {return adminService.findById(adminId);}

    /**
     * READ: A controller method for handling searching for a specific Admin
     * @param adminSSN
     *  The SSN of the specific Admin
     * @return
     *  The Admin with the matching SSN if they exist
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("find-by/ssn/{adminSSN}")
    public Admin getAdminViaSSN (@PathVariable("adminSSN") String adminSSN) {return adminService.findBySSN(adminSSN);}

    /**
     * READ: A controller method for handling searching for a specific Admin
     * @param adminUserName
     *  The username of the specific Admin
     * @return
     *  The Admin with the matching username if they exist
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("find-by/username/{adminUserName}")
    public Admin getAdminViaUserName (@PathVariable("adminUserName") String adminUserName) {return adminService.findByUserName(adminUserName);}

    /**
     * READ: A controller method for handling searching for a specific Admin
     * @param adminFirstName
     *  The first name of the specific Admin
     * @return
     *  A List of Admin(s) with a matching first name
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("find-by/first-names/{adminFirstName}")
    public List<Admin> getAdminsViaFirstName (@PathVariable("adminFirstName") String adminFirstName) {return adminService.findByFirstName(adminFirstName);}

    /**
     * READ: A controller method for handling searching for a specific Admin
     * @param adminLastName
     *  The last name of the specific Admin
     * @return
     *  A List of Admin(s) with a matching last name
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("find-by/last-names/{adminLastName}")
    public List<Admin> getAdminsViaLastName (@PathVariable("adminLastName") String adminLastName) {return adminService.findByLastName(adminLastName);}

    /**
     * READ: A controller method for handling searching for a specific Admin
     * @param adminEmail
     *  The email of the specific Admin
     * @return
     *  The Admin with the matching email if they exist
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("find-by/email/{adminEmail}")
    public Admin getAdminsViaEmail (@PathVariable("adminEmail") String adminEmail) {return adminService.findByEmail(adminEmail);}

    /**
     * READ: A controller method for handling searching for a specific Admin
     * @param adminPhoneNumber
     *  The phone number of the specific Admin
     * @return
     *  The Admin with the matching phone number if they exist
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("find-by/phone-number/{adminPhoneNumber}")
    public Admin getAdminsViaPhoneNumber (@PathVariable("adminPhoneNumber") String adminPhoneNumber) {return adminService.findByPhoneNumber(adminPhoneNumber);}

    /**
     * READ: A controller method for handling searching for a specific Admin
     * @param adminAccessLevel
     *  The access level of the specific Admin
     * @return
     *  A List of Admin(s) with a matching access level
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("find-by/access-level/{adminAccessLevel}")
    public List<Admin> getAdminsViaAccessLevel (@PathVariable("adminAccessLevel") AccessLevel adminAccessLevel) {return adminService.findByAccessLevel(adminAccessLevel);}

    /**
     * READ: A controller method for handling searching for a specific Admin
     * @param adminManagementTitle
     *  The management title of the specific Admin
     * @return
     *  A List of Admin(s) with a matching management title
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("find-by/management-title/{adminManagementTitle}")
    public List<Admin> getAdminsViaManagementTitle (@PathVariable("adminManagementTitle") ManagementTitle adminManagementTitle) {return adminService.findByManagementTitle(adminManagementTitle);}

    /**
     * READ: A controller method for handling searching for all Admins
     * @return
     *  A List of all existing Admins
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("/find/all")
    public List<Admin> admins () { return adminService.findAll(); }

    /**
     * READ: A controller method that handles getting all shifts for a specific Admin
     * @param adminId
     *  The ID of the Admin
     * @return A list of shifts for the matching Admin
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("/{adminId}/all-shifts")
    public List<Shift> getShifts (@PathVariable("adminId") Integer adminId) {return shiftService.findAllEmployeeShifts(adminId);}

    /**
     * READ: A controller method that handles getting weekly shifts for a specific Admin
     * @param adminId
     *  The ID of the Admin
     * @param startDay
     *  The start date of the week
     * @return A list of shifts for the matching Admin within the week
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("/{adminId}/weekly-shifts/{startDay}")
    public List<Shift> getWeeklyShifts (@PathVariable("adminId") Integer adminId, @PathVariable("startDay") LocalDate startDay) {return shiftService.findWeeklyEmployeeShifts(adminId, startDay);}

    /**
     * UPDATE: A controller method for handling updating an existing Admin
     * @param adminUpdateRequest
     *  The DTO containing the new Admin information from a JSON
     * @return A message confirming a successful information update
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @PutMapping("/update")
    public String updateAdmin (@RequestBody UpdateEmployeeRequest adminUpdateRequest) {return adminService.updateAdmin(adminUpdateRequest).getMessage();}

    /**
     * READ: Returns all PENDING update requests sent to admins.
     * @return
     *  A list of all pending notifications awaiting admin review
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("/notifications/pending")
    public List<Notification> getPendingNotifications() {return notificationService.findByRecipientAndStatus(AccessLevel.ADMIN, Status.PENDING);}

    /**
     * READ: Returns all notifications for the ADMIN access level regardless of status.
     * @return
     *  A list of all admin notifications
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("/notifications/all")
    public List<Notification> getAllAdminNotifications() {return notificationService.findByRecipient(AccessLevel.ADMIN);}

    /**
     * READ: Returns a single notification by its ID.
     * @param notificationId
     *  The ID of the notification
     * @return
     *  The matching notification
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @GetMapping("/notifications/{notificationId}")
    public Notification getNotificationById(@PathVariable Integer notificationId) {return notificationService.findById(notificationId);}

    /**
     * UPDATE: Approves a pending employee update request.
     * Deserializes the stored request JSON, applies the changes to the employee
     * record, and marks the notification as APPROVED.
     * @param notificationId
     *  The ID of the pending notification to approve
     * @return
     *  Confirmation message
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @PutMapping("/notifications/{notificationId}/approve")
    public String approveUpdateRequest(@PathVariable Integer notificationId) {
        notificationService.approveUpdateRequest(notificationId);
        return "Update request approved.";
    }

    /**
     * UPDATE: Rejects a pending employee update request.
     * Marks the notification as REJECTED without applying any changes.
     * @param notificationId
     *  The ID of the pending notification to reject
     * @return
     *  Confirmation message
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @PutMapping("/notifications/{notificationId}/reject")
    public String rejectUpdateRequest(@PathVariable Integer notificationId) {
        notificationService.rejectUpdateRequest(notificationId);
        return "Update request rejected.";
    }

    /**
     * DELETE: Deletes a specific notification by its ID.
     * @param notificationId
     *  The ID of the notification to delete
     * @return
     *  True if completed
     */
    @RequiresAccess(AccessLevel.ADMIN)
    @DeleteMapping("/notifications/{notificationId}")
    public Boolean deleteNotification(@PathVariable Integer notificationId) {
        notificationService.deleteNotification(notificationId);
        return true;
    }

    /**
     * DELETE: A controller method for handling specific Admin deletion
     * @param adminId
     *  The EmployeeID of the Admin
     * @return True if completed
     */
    @RequiresAccess(AccessLevel.GENERAL_MANAGER)
    @DeleteMapping("/delete/{adminId}")
    public Boolean deleteAdmin (@PathVariable Integer adminId) {
        adminService.deleteAdmin(adminId);
        return true;
    }

    /**
     * DELETE: A controller method for handling deleting all Admins
     * @return True if completed
     */
    @RequiresAccess(AccessLevel.GENERAL_MANAGER)
    @DeleteMapping("/delete/all")
    public Boolean deleteAllAdmins () {
        adminService.deleteAllAdmin();
        return true;
    }
}