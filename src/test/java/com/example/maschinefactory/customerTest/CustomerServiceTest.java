package com.example.maschinefactory.customerTest;

import com.example.maschinefactory.address.Address;
import com.example.maschinefactory.customer.*;
import com.example.maschinefactory.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(CustomerService.class)
@Import(SecurityConfig.class)
public class CustomerServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private CustomerValidation customerValidation;

    private CustomerService customerService;

    private List<Customer> customers;

    private List<Address> addresses;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerRepository, customerValidation);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ShouldFindAllCustomers() {
        Pageable pageable = Pageable.unpaged();

        // Create test data
        List<Customer> customerList = Arrays.asList(
                new Customer(1L, "Customer1", "email1@test.com", "pass1", "1234567890", true),
                new Customer(2L, "Customer2", "email2@test.com", "pass2", "1234567891", false)
        );

        // Initialize expectedPage with test data
        Page<Customer> expectedPage = new PageImpl<>(customerList, pageable, customerList.size());

        // Set up mock behavior
        when(customerRepository.findAll(pageable)).thenReturn(expectedPage);

        // Call the method under test
        Page<Customer> actualPage = customerService.findAllCustomers(pageable);

        // Assertions
        assertEquals(expectedPage, actualPage);
        verify(customerRepository).findAll(pageable);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ShouldCreateNewCustomer() throws InvalidCustomerDataException {
        Customer customer = new Customer(4L, "ole", "ole@example.com", "password", "1234567890", true);

        when(customerValidation.validateCustomerData(customer)).thenReturn(true);
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer createdCustomer = customerService.createCustomer(customer);

        assertNotNull(createdCustomer);
        assertEquals(customer.getEmail(), createdCustomer.getEmail());
        verify(customerRepository).save(customer);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ShouldNotCreateCustomerAndThrowDataInvalid() {
        Customer customer = new Customer(2L, "boss", "boss@example.com", "", "0987654321", false);

        when(customerValidation.validateCustomerData(customer)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            customerService.createCustomer(customer);
        });

        verify(customerRepository, never()).save(any(Customer.class));
    }


}