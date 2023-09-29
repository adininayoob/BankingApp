package com.banking.App.model;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "admins")
public class Admin {
	private static final Logger logger = LoggerFactory.getLogger(Admin.class);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;

    @ManyToMany
    @JoinTable(
      name = "admin_customer", 
      joinColumns = @JoinColumn(name = "admin_id"), 
      inverseJoinColumns = @JoinColumn(name = "customer_id"))
    @JsonManagedReference
    private Set<Customer> customers= new HashSet<>();

    // Constructors
    public Admin() {
        // Default constructor
    }

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
    	logger.info("Setting new customer");
        this.customers = customers;
    }
}
