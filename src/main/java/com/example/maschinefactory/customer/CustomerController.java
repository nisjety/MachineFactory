package com.example.maschinefactory.customer;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    CustomerController(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;}

   @GetMapping("")
   Iterable<Customer> findAll() {
       return customerRepository.findAll();
   }

    @GetMapping("/{customerId}")
    Optional<Customer> findById(@PathVariable Long customerId) {
        return Optional.ofNullable(customerRepository.findById(customerId)
                .orElseThrow(CustomerNotFound::new));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    Customer createCustomer(@RequestBody @Validated Customer customer){
        return customerRepository.save(customer);
    }
}
