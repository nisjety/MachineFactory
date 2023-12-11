package com.example.maschinefactory.customer;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Component
public class CustomerValidation {

    private final CustomerRepository customerRepository;
    private final PasswordHasher passwordHasher;

    // Constructor
    public CustomerValidation(CustomerRepository customerRepository, PasswordHasher passwordHasher) {
        this.customerRepository = customerRepository;
        this.passwordHasher = passwordHasher;
    }

    public boolean validateCustomerData(Customer customer) {
        if (StringUtils.isEmpty(customer.getName())) {
            return false;
        }

        if (!isValidEmail(customer.getEmail())) {
            return false;
        }

        if (!isStrongPassword(customer.getPassword())) {
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        // Add your email validation logic (e.g., using regex)
        return Pattern.matches("your-email-regex", email);
    }

    private boolean isStrongPassword(String password) {
        // Add your strong password validation logic
        return Pattern.matches("your-password-regex", password);
    }

    public void validateExistingCustomer(Long customerId, Customer updatedCustomer) {
        // Validation logic for existing customer
        customerRepository.findById(customerId)
                .orElseThrow(CustomerNotFoundException::new);
    }

    public void validateCustomerCredentials(String email, String password) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(CustomerNotFoundException::new);
        if (!passwordHasher.checkPassword(password, customer.getPassword())) {
            throw new InvalidPasswordException();
        }
    }

    public void validateCustomerForDeletion(String email, String password) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(CustomerNotFoundException::new);
        if (!passwordHasher.checkPassword(password, customer.getPassword())) {
            throw new InvalidPasswordException();
        }
    }
}
