package com.example.maschinefactory.customer;

import com.example.maschinefactory.address.Address;
import com.example.maschinefactory.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerValidation customerValidation;

    public CustomerService(CustomerRepository customerRepository, CustomerValidation customerValidation) {
        this.customerRepository = customerRepository;
        this.customerValidation = customerValidation;
    }

    public Page<Customer> findAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }


    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Page<Customer> findCustomersByName(String name, Pageable pageable) {
        return customerRepository.findByName(name, pageable);
    }

    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Page<Customer> findCustomersByActiveStatus(boolean isActive, Pageable pageable) {
        return customerRepository.findByActive(isActive, pageable);
    }

    public Optional<Customer> findCustomersByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }

    public Customer createCustomer(Customer customer) throws InvalidCustomerDataException {
        if (customerValidation.validateCustomerData(customer)) {
            return customerRepository.save(customer);
        } else {
            try {
                throw new InvalidCustomerDataException();
            } catch (InvalidCustomerDataException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Transactional
    public Customer updateCustomer(Long customerId, Customer customer) throws InvalidCustomerDataException {
        if (customerValidation.validateCustomerData(customer)) {
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
            } else {
                throw new CustomerNotFoundException();
            }
        } else {
            try {
                throw new InvalidCustomerDataException();
            } catch (InvalidCustomerDataException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{email}")
    public void deleteCustomer(String email, String password) {
        customerValidation.validateCustomerCredentials(email, password);
        customerRepository.deleteByEmail(email);
    }

    public List<Address> getAddressForCustomer(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            return new ArrayList<>(customer.get().getAddresses());
        } else {
            throw new CustomerNotFoundException();
        }
    }

    public List<Order> getOrdersForCustomer(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            return new ArrayList<>(customer.get().getOrders());
        } else {
            throw new CustomerNotFoundException();
        }
    }



    @Transactional
    public Customer addOrderToCustomer(Long customerId, Order order) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(CustomerNotFoundException::new);
        customer.getOrders().add(order);
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer addAddressToCustomer(Long customerId, Address address) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(CustomerNotFoundException::new);
        customer.getAddresses().add(address);
        return customerRepository.save(customer);
    }

    @Transactional
    public void removeOrderFromCustomer(Long customerId, long orderId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(CustomerNotFoundException::new);
        customer.getOrders().removeIf(order -> order.getOrderId() == orderId);
    }

    @Transactional
    public void removeAddressFromCustomer(Long customerId, long addressId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(CustomerNotFoundException::new);
        customer.getAddresses().removeIf(address -> address.getAddressId() == addressId);
    }
}


