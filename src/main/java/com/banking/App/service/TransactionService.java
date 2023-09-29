package com.banking.App.service;
//import exceptionHandler.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.App.model.Customer;
import com.banking.App.model.Transaction;
import com.banking.App.repository.CustomerRepository;
import com.banking.App.repository.TransactionRepository;

@Service
@Transactional
public class TransactionService {
	private static final Logger logger = Logger.getLogger(TransactionService.class.getName());

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;
    public boolean authenticate(String accountNumber, String password) {
        logger.info("Authenticating customer with account number: " + accountNumber);
        Customer account = customerRepository.findByAccountNumber(accountNumber);
        if (account != null && password.equals(account.getPassword())) {
            logger.info("Authentication successful for account number: " + accountNumber);
            return true;
        }
        logger.warning("Authentication failed for account number: " + accountNumber);
        return false;
    }

    public Transaction depositMoney(String accountNumber, Double amount) {
        logger.info("Depositing amount " + amount + " to account number: " + accountNumber);
        Customer customer = customerRepository.findByAccountNumber(accountNumber);
        if (customer != null) {
            customer.setInitialBalance(customer.getInitialBalance() + amount);
            customerRepository.save(customer);
            logger.info("Deposit successful. New balance: " + customer.getInitialBalance());

            Transaction transaction = new Transaction();
            transaction.setCustomer(customer);
            transaction.setAmount(amount);
            transaction.setType("DEPOSIT");
            logger.info("Creating deposit transaction record for account number: " + accountNumber);
            return transactionRepository.save(transaction);
        }
        logger.warning("Account not found for account number: " + accountNumber);
        return null;
    }

    public Transaction withdrawMoney(String accountNumber, Double amount) {
        logger.info("Withdrawing amount " + amount + " from account number: " + accountNumber);
        Customer customer = customerRepository.findByAccountNumber(accountNumber);
        if (customer != null) {
            double newBalance = customer.getInitialBalance() - amount;
            if (newBalance >= 0) {
                customer.setInitialBalance(newBalance);
                customerRepository.save(customer);
                logger.info("Withdrawal successful. New balance: " + customer.getInitialBalance());

                Transaction transaction = new Transaction();
                transaction.setCustomer(customer);
                transaction.setAmount(amount);
                transaction.setType("WITHDRAWAL");
                logger.info("Creating withdrawal transaction record for account number: " + accountNumber);
                return transactionRepository.save(transaction);
            } else {
                logger.warning("Insufficient balance for withdrawal in account number: " + accountNumber);
                throw new IllegalArgumentException("Insufficient balance.");
            }
        }
        logger.warning("Account not found for account number: " + accountNumber);
        return null;
    }

}
