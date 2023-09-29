package com.banking.App.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking.App.model.Customer;
import com.banking.App.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(
            @RequestParam("accountNumber") String accountNumber,
            @RequestParam("password") String password) {
        Customer customer = customerService.loginCustomer(accountNumber, password);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(401).body("Incorrect account number or password.");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam("accountNumber") String accountNumber,
            @RequestParam("newPassword") String newPassword) {
        customerService.changePassword(accountNumber, newPassword);
        return ResponseEntity.ok("Password changed successfully.");
    }

    // Log out a customer
    @GetMapping("/logout")
    public ResponseEntity<String> logoutCustomer() {
        customerService.logoutCustomer();
        return ResponseEntity.ok("Logged out successfully.");
    }
}
