package com.banking.App.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.banking.App.model.Customer;
import com.banking.App.repository.CustomerRepository;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class CustomerService {
	private static final Logger logger = Logger.getLogger(CustomerService.class.getName());

    @Autowired
    private CustomerRepository customerRepository;


    public Customer loginCustomer(String accountNumber, String password) {
        logger.info("Attempting to login customer with account number: " + accountNumber);
        Optional<Customer> optionalCustomer = Optional.ofNullable(customerRepository.findByAccountNumber(accountNumber));

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if (password.equals(customer.getPassword())) {  // changed '==' to 'equals' for string comparison
                logger.info("Login successful for account number: " + accountNumber);
                return customer;
            } else {
                logger.warning("Password mismatch for account number: " + accountNumber);
            }
        } else {
            logger.warning("No customer found with account number: " + accountNumber);
        }

        return null;
    }

    public void changePassword(String accountNumber, String newPassword) {
        logger.info("Attempting to change password for account number: " + accountNumber);
        Customer customer = customerRepository.findByAccountNumber(accountNumber);
        if (customer != null) {
            customer.setPassword(newPassword);
            customerRepository.save(customer);
            logger.info("Password changed successfully for account number: " + accountNumber);
        } else {
            logger.warning("No customer found with account number: " + accountNumber);
            throw new IllegalArgumentException("No customer found with account number: " + accountNumber);
        }
    }

    public void logoutCustomer() {
        logger.info("Logging out customer");
        SecurityContextHolder.clearContext();
    }

}
