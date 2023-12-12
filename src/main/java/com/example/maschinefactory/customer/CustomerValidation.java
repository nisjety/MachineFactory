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
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"; // Simple regex pattern for email
        return Pattern.matches(emailRegex, email);
    }


    private boolean isStrongPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        // Explanation:
        // ^                 # start-of-string
        // (?=.*[0-9])       # a digit must occur at least once
        // (?=.*[a-z])       # a lower case letter must occur at least once
        // (?=.*[A-Z])       # an upper case letter must occur at least once
        // (?=.*[@#$%^&+=])  # a special character must occur at least once
        // (?=\S+$)          # no whitespace allowed in the entire string
        // .{8,}             # anything, at least eight places though
        // $                 # end-of-string
        return Pattern.matches(passwordRegex, password);
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
