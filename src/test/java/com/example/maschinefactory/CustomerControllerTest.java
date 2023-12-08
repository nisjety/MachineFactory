package com.example.maschinefactory;


import com.example.maschinefactory.customer.*;
import com.example.maschinefactory.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Import(SecurityConfig.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private CustomerService customerService;

    private List<Customer> customers;

    @BeforeEach
    void setUp() {
        customers = List.of(
                new Customer(1, "ole", "test1@example.com", "password", "023456789", true),
                new Customer(2, "john", "test2@example.com", "password2", "123456789", true),
                new Customer(3, "karma", "test3@example.com", "password3", "223456789", false)
        );

        when(customerRepository.findAll()).thenReturn(customers);
    }

    @Test
    @WithMockUser
    void shouldFindAllUsers() throws Exception {
        String jsonResponse = """
                [
                    {
                      "customerId": 1,
                      "name": "ole",
                      "email": "test1@example.com",
                      "password": "password",
                      "phoneNumber": "023456789",
                      "active": true
                    },
                    {
                      "customerId": 2,
                      "name": "john",
                      "email": "test2@example.com",
                      "password": "password2",
                      "phoneNumber": "123456789",
                      "active": true
                    },
                    {
                      "customerId": 3,
                      "name": "karma",
                      "email": "test3@example.com",
                      "password": "password3",
                      "phoneNumber": "223456789",
                      "active": false
                    }
                  ]
                """;
        when(customerRepository.findAll()).thenReturn(customers);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    @WithMockUser
    void shouldFindCustomerBasedOnId() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customers.get(0)));

        var customer = customers.get(0);
        var json = "{"
                + "\"customerId\":" + customer.getCustomerId() + ","
                + "\"name\":\"" + customer.getName() + "\","
                + "\"email\":\"" + customer.getEmail() + "\","
                + "\"password\":\"" + customer.getPassword() + "\","
                + "\"phoneNumber\":\"" + customer.getPhoneNumber() + "\","
                + "\"active\":" + customer.isActive()
                + "}";

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @WithMockUser
    void shouldGetCustomerNotExists() throws Exception {
        when(customerRepository.findById(99L)).thenThrow(CustomerNotFoundException.class);
        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateNewCustomerIfCustomerValid() throws Exception {
        var customer = new Customer(4L, "bole", "test4@example.com", "password4", "323456789", true);
        when(customerRepository.save(customer)).thenReturn(customer);
        String json = "{"
                + "\"customerId\":" + customer.getCustomerId() + ","
                + "\"name\":\"" + customer.getName() + "\","
                + "\"email\":\"" + customer.getEmail() + "\","
                + "\"password\":\"" + customer.getPassword() + "\","
                + "\"phoneNumber\":\"" + customer.getPhoneNumber() + "\","
                + "\"active\":" + customer.isActive()
                + "}";

        mockMvc.perform(post("/api/customers")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotCreateCustomerIfInvalid() throws Exception {
        String json = """
            {
                "customerId":"",
                "name": "bole",
                "email": "",
                "password": "",
                "phoneNumber": "323456789",
                "active": true
            }
            """;

        mockMvc.perform(post("/api/customers")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateCustomerIfValidInput() throws Exception {
        Customer updated = new Customer(2L, "john", "UpdatedTest2@example.com", "UpdatedPassword2", "Updated123456789", true);
        when(customerRepository.save(updated)).thenReturn(updated);
        when(customerRepository.findById(2L)).thenReturn(Optional.of(updated));
        String requestBody = "{"
                + "\"customerId\":" + updated.getCustomerId() + ","
                + "\"name\":\"" + updated.getName() + "\","
                + "\"email\":\"" + updated.getEmail() + "\","
                + "\"password\":\"" + updated.getPassword() + "\","
                + "\"phoneNumber\":\"" + updated.getPhoneNumber() + "\","
                + "\"active\":" + updated.isActive()
                + "}";

        mockMvc.perform(put("/api/customers/2")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteCustomerIfCustomerEmailValid() throws Exception {
        Customer customer = new Customer(3, "karma", "test3@example.com", "password3", "223456789", false);
        when(customerRepository.findByEmail("test3@example.com")).thenReturn(Optional.of(customer));
        when(customerService.validatePassword(customer, "password3")).thenReturn(true);

        mockMvc.perform(delete("/api/customers/test3@example.com")
                        .param("password", "password3"))
                .andExpect(status().isNoContent());
    }
}
