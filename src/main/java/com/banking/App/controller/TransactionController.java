package com.banking.App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.banking.App.model.Transaction;
import com.banking.App.service.TransactionService;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
            @RequestParam Double amount,
            @RequestParam String accountNumber,
            @RequestParam String password) {
        try {
            if (transactionService.authenticate(accountNumber, password)) {
                Transaction transaction = transactionService.depositMoney(accountNumber, amount);
                return new ResponseEntity<>("Transaction completed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(
            @RequestParam Double amount,
            @RequestParam String accountNumber,
            @RequestParam String password) {
        try {
            if (transactionService.authenticate(accountNumber, password)) {
                Transaction transaction = transactionService.withdrawMoney(accountNumber, amount);
                return new ResponseEntity<>("Transaction completed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

