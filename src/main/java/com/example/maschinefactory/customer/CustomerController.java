package com.example.maschinefactory.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("")
    public Page<Customer> findAll(Pageable pageable) {
        return customerService.findAllCustomers(pageable);
    }


    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable Long customerId) {
        return customerService.findCustomerById(customerId)
                .map(ResponseEntity::ok)
                .orElseThrow(CustomerNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    Customer createCustomer(@RequestBody @Validated Customer customer) throws InvalidCustomerDataException {
        return customerService.createCustomer(customer);
    }

    @PutMapping("/{customerId}")
    Customer updateCustomer(@PathVariable Long customerId, @RequestBody @Validated Customer customer) throws InvalidCustomerDataException {
        return customerService.updateCustomer(customerId, customer);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{email}")
    public void deleteCustomer(@PathVariable String email, @RequestParam String password) {
        customerService.deleteCustomer(email, password);
    }
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPassword(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}