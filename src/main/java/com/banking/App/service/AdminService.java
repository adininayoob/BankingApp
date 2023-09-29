package com.banking.App.service;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.App.model.Admin;
import com.banking.App.model.Customer;
import com.banking.App.repository.AdminRepository;
import com.banking.App.repository.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

@Service
@Transactional
public class AdminService {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CustomerRepository customerRepository;


    public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Admin registerCustomer(String adminUsername, Customer newCustomer) {
        logger.info("Attempting to register customer for admin: {}", adminUsername);

        Admin admin = getAdminByUsername(adminUsername);
        
        logger.info("Checking if admin exists");
        if (admin == null) {
            logger.error("No such admin found: {}", adminUsername);
            throw new IllegalArgumentException("No such admin found.");
        }
        
        logger.info("Validating customer data");
        validateCustomerData(newCustomer);

        String accountNumber = generateAccountNumber();
        String temporaryPassword = generateTemporaryPassword();
       
        newCustomer.setAccountNumber(accountNumber);
        newCustomer.setPassword(temporaryPassword);
        

        logger.info("Generated account number: {}, temporary password for customer: {}", accountNumber, temporaryPassword);
        
        logger.info("Admin before update: " + admin);
        logger.info("Customer before update: " + newCustomer);

        try {
            admin.getCustomers().add(newCustomer);
            newCustomer.getAdmins().add(admin);
        } catch (Exception e) {
            logger.error("Error while adding new customer to admin: ", e);
        }


        logger.info("Admin after update: " + admin);
        logger.info("Customer after update: " + newCustomer);

        logger.info("Saving new customer and updating admin data...");
        customerRepository.save(newCustomer);
        adminRepository.save(admin);

        logger.info("Customer registration successful for admin: {}", adminUsername);
        return admin;
    }


    private void validateCustomerData(Customer newCustomer) {
        if (newCustomer.getFullName() == null || newCustomer.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name is required.");
        }
    }

    private String generateAccountNumber() {
        return String.format("%010d", new Random().nextInt(1_000_000_000));
    }

    private String generateTemporaryPassword() {
        return String.format("%08d", new Random().nextInt(100_000_000));
    }
}