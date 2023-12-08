package com.example.maschinefactory.customer;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public boolean validatePassword(Customer customer, String password) {
        return customer.getPassword().equals(password);
    }

    // Other methods...
}
