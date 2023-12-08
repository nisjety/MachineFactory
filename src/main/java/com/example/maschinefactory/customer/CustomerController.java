package com.example.maschinefactory.customer;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    CustomerController(CustomerRepository customerRepository, CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @GetMapping("")
    Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }

    @GetMapping("/{customerId}")
    Optional<Customer> findById(@PathVariable Long customerId) {
        return Optional.ofNullable(customerRepository.findById(customerId)
                .orElseThrow(CustomerNotFoundException::new));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    Customer createCustomer(@RequestBody @Validated Customer customer) {
        return customerRepository.save(customer);
    }

    @PutMapping("/{customerId}")
    Customer updateCustomer(@PathVariable Long customerId, @RequestBody @Validated Customer customer) {
        Optional<Customer> existing = customerRepository.findById(customerId);
        if (existing.isPresent()) {
            Customer updated = new Customer(
                    existing.get().getCustomerId(),
                    existing.get().getName(),
                    customer.getEmail(),
                    customer.getPassword(),
                    customer.getPhoneNumber(),
                    existing.get().isActive()
            );
            return customerRepository.save(updated);
        }else{
            throw new CustomerNotFoundException();
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{email}")
    public void deleteCustomer(@PathVariable String email, @RequestParam String password) {
        Optional<Customer> existingCustomer = customerRepository.findByEmail(email);

        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();
            if (customerService.validatePassword(customer, password)) {
                customerRepository.deleteByEmail(email);
            } else {
                throw new InvalidPasswordException(); // Custom exception
            }
        } else {
            throw new CustomerNotFoundException(); // Custom exception
        }
    }
}
