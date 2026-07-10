package org.example.inventory_manager_beta1.MainApplicationClasses;

import org.example.inventory_manager_beta1.Entities.*;
import org.example.inventory_manager_beta1.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/groupchat")
public class GroupChatController {

    @Autowired
    private GroupChatRepository groupRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private AdminRepository adminRepo;

    // Create group
    @PostMapping("/create")
    public GroupChat createGroup(@RequestParam String roomName, @RequestParam Integer adminId) {
        Admin admin = adminRepo.findById(adminId).orElseThrow();

        GroupChat group = new GroupChat();
        group.setRoomName(roomName);
        group.setAdmin(admin);

        return groupRepo.save(group);
    }

    // Add members
    @PostMapping("/addMember")
    @Transactional
    public GroupChat addMember(@RequestParam Long groupId, @RequestParam Integer employeeId) {
        GroupChat group = groupRepo.findById(groupId).orElseThrow();
        Employee emp = employeeRepo.findById(employeeId).orElseThrow();

        group.getMembers().add(emp);
        return groupRepo.save(group);
    }

    @GetMapping("/{roomName}")
    @Transactional
    public GroupChat getGroup(@PathVariable String roomName) {
        return groupRepo.findByRoomName(roomName).orElseThrow();
    }
}
