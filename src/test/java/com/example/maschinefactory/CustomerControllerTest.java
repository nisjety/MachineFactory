package com.example.maschinefactory;

import com.example.maschinefactory.customer.Customer;
import com.example.maschinefactory.customer.CustomerController;
import com.example.maschinefactory.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    private List<Customer> customers;

    @BeforeEach
    void setUp() {
        customers = List.of(
                new Customer(1, "ole", "test1@example.com", "password", "023456789", true),
                new Customer(2, "john", "test2@example.com", "password2", "123456789", true),
                new Customer(3, "karma", "test3@example.com", "password3", "2234567890", false)
        );

        Mockito.when(customerRepository.findAll()).thenReturn(customers);
    }

    @Test
    @WithMockUser
    void shouldFindAllUsers() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk());
    }

    // Additional test methods...
}
