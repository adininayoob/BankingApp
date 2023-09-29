package com.banking.App.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.banking.App.model.Customer;
import com.banking.App.service.AdminService;

import org.springframework.http.ResponseEntity;
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/register-customer")
    public ResponseEntity<String> registerCustomerForm() {
    	return ResponseEntity.ok("Success!");
    }
    
    @PostMapping("/register-customer")
    public ResponseEntity<String> registerCustomer(
            @RequestParam String adminUsername,
            @RequestParam String fullName,
            @RequestParam String address,
            @RequestParam String mobileNo,
            @RequestParam String email,
            @RequestParam String accountType,
            @RequestParam Double initialBalance,
            @RequestParam String idProof) {

        Customer newCustomer = new Customer();
        newCustomer.setFullName(fullName);
        newCustomer.setAddress(address);
        newCustomer.setMobileNo(mobileNo);
        newCustomer.setEmail(email);
        newCustomer.setAccountType(accountType);
        newCustomer.setInitialBalance(initialBalance);
        newCustomer.setIdProof(idProof);

        try {
            adminService.registerCustomer(adminUsername, newCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error registering customer");
        }

        return ResponseEntity.ok("Customer registered successfully!");
    }

}
