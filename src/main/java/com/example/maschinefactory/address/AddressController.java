package com.example.maschinefactory.address;

import com.example.maschinefactory.customer.Customer;
import com.example.maschinefactory.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    private final AddressService addressService;

    AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("")
    public Page<Address> findAll(Pageable pageable) {
        return addressService.findAllAddress(pageable);
    }


    @GetMapping("/{addressId}")
    public ResponseEntity<Address> findAddressById(@PathVariable Long addressId) {
        return addressService.findAddressById(addressId)
                .map(ResponseEntity::ok)
                .orElseThrow(AddressNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    Address createAddress(@RequestBody @Validated Address address) throws InvalidAddressDataException {
        return addressService.createAddress(address);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{addressId}")
    Address updateAddress(@PathVariable Long addressId, @RequestBody @Validated Address address) throws InvalidAddressDataException {
        return addressService.updateAddress(addressId, address);
    }

    @GetMapping("/{addressId}/customers")
    public ResponseEntity<List<Customer>> getCustomersFromAddress(@PathVariable Long addressId) {
        List<Customer> customers = addressService.getCustomersFromAddress(addressId);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{addressId}/orders")
    public ResponseEntity<List<Order>> getOrdersForAddress(@PathVariable Long addressId) {
        List<Order> orders = addressService.getOrdersForAddress(addressId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{addressId}/orders")
    public addOrderToAddress(Long addressId, Order order) {
        Address address = addressService.findById(addressId);
    }

    @PutMapping("/{addressId}/customers")
    public addCustomerToAddress(Long addressId, Customer customer) {
        Address address = addressService.findById(addressId);
    }
}
