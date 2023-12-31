package com.example.maschinefactory.customer;

import com.example.maschinefactory.address.Address;
import com.example.maschinefactory.address.AddressNotFoundException;
import com.example.maschinefactory.order.*;
import com.example.maschinefactory.order.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

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
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer createCustomer(@RequestBody @Validated Customer customer) throws InvalidCustomerDataException {
        return customerService.createCustomer(customer);
    }


    @ResponseStatus(HttpStatus.ACCEPTED)
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

    @GetMapping("/{customerId}/addresses")
    public ResponseEntity<List<Address>> getAddressForCustomer(@PathVariable Long customerId) {
        List<Address> addresses = customerService.getAddressForCustomer(customerId);
        return ResponseEntity.ok(addresses);
    }

    @PutMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<Address> updateCustomerAddress(@PathVariable Long customerId, @PathVariable Long addressId, @RequestBody Address address) {
        Customer updatedCustomer = customerService.updateCustomerAddress(customerId, addressId, address);
        Address updatedAddress = updatedCustomer.getAddresses().stream()
                .filter(a -> a.getAddressId() == addressId)
                .findFirst()
                .orElseThrow(() -> new AddressNotFoundException());

        return ResponseEntity.ok(updatedAddress);
    }

//change to page
    @GetMapping("/{customerId}/orders")
    public ResponseEntity<List<OrderEntity>> getOrdersForCustomer(@PathVariable Long customerId) {
        List<OrderEntity> orders = customerService.getOrdersForCustomer(customerId);
        return ResponseEntity.ok(orders);
    }
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{customerId}/orders")
    Customer addOrderToCustomer(@PathVariable Long customerId, @RequestBody OrderEntity orderEntity) {
        return customerService.addOrderToCustomer(customerId, orderEntity);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{customerId}/addresses")
    Customer addAddressToCustomer(@PathVariable Long customerId, @RequestBody Address address) {
        return customerService.addAddressToCustomer(customerId, address);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{customerId}/orders/{orderId}")
    public ResponseEntity<?> removeOrderFromCustomer(@PathVariable Long customerId, @PathVariable Long orderId) {
        customerService.removeOrderFromCustomer(customerId, orderId);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<?> removeAddressFromCustomer(@PathVariable Long customerId, @PathVariable Long addressId) {
        customerService.removeAddressFromCustomer(customerId, addressId);
        return ResponseEntity.noContent().build();
    }
}