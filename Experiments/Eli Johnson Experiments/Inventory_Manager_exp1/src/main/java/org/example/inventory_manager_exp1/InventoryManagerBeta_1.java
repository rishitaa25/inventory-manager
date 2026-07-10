package org.example.inventory_manager_exp1;

import org.example.inventory_manager_exp1.entities.Admin;
import org.example.inventory_manager_exp1.entities.Employee;
import org.example.inventory_manager_exp1.repositories.AdminRepository;
import org.example.inventory_manager_exp1.repositories.DeliveryRepository;
import org.example.inventory_manager_exp1.repositories.EmployeeRepository;
import org.example.inventory_manager_exp1.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@Controller
public class InventoryManagerBeta_1 {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ItemRepository itemRepository;

    ArrayList<String> Accounts = new ArrayList<>();
    ArrayList<Employee> Employees = new ArrayList<>();
    ArrayList<Admin> Admins = new ArrayList<>();

    @GetMapping("/hash/{password}")
    public String hashPassword(@PathVariable String password) {
        String hashedPassword = passwordEncoder.encode(password);
        return "Raw password: " + password + "<br>hashed password: " + hashedPassword;
    }

    @GetMapping("/signup")
    public String signup() {
        return "Signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password) {
        String hashedPassword = passwordEncoder.encode(password);
        String Account = "Username: " + username + "\nPassword: " + password + "\nHashed password: " + hashedPassword;
        Accounts.add(Account);
        if (Employees.isEmpty()) {
            Admins.add(new Admin (123, username, "Admin 1", "Admin1", hashedPassword, 3, "admin@Admin.com", "123-456-7890"));
            Employees.add(new Employee (123, username, "Admin 1", "Admin1", hashedPassword, 3, "admin@Admin.com", "123-456-7890"));
        }
        else {
            Employees.add(new Employee (123, username, "Admin 1", "Admin1", hashedPassword, 3, "admin@Admin.com", "123-456-7890"));
        }
        return "Signup";
    }

    @GetMapping("/accounts")
    public ArrayList<String> accounts(Model model) {
        model.addAttribute("accounts", Accounts);
        return Accounts;
    }

    @GetMapping("/employees")
    @ResponseBody
    public ArrayList<Employee> employees(Model model) {
        model.addAttribute("employees", Employees);
        return Employees;
    }

    @GetMapping("/admins")
    @ResponseBody
    public ArrayList<Admin> Admins(Model model) {
        model.addAttribute("managers", Admins);
        return Admins;
    }
}
