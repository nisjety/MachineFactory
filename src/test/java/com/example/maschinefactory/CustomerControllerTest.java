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
    private CustomerService customerService;

    private List<Customer> customers;

    @BeforeEach
    void setUp() {
        customers = List.of(
                new Customer(1L, "ole", "test1@example.com", "password", "023456789", true),
                new Customer(2L, "john", "test2@example.com", "password2", "123456789", true),
                new Customer(3L, "karma", "test3@example.com", "password3", "223456789", false)
        );
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
        when(customerService.findAllCustomers()).thenReturn(customers);
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    @WithMockUser
    void shouldFindCustomerBasedOnId() throws Exception {
        when(customerService.findCustomerById(1L)).thenReturn(Optional.of(customers.get(0)));

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
        when(customerService.findCustomerById(99L)).thenThrow(CustomerNotFoundException.class);
        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateNewCustomerIfCustomerValid() throws Exception {
        Customer newCustomer = new Customer(4L, "bole", "test4@example.com", "password4", "323456789", true);
        when(customerService.createCustomer(any(Customer.class))).thenReturn(newCustomer);

        String json = "{"
                + "\"customerId\": 4,"
                + "\"name\": \"bole\","
                + "\"email\": \"test4@example.com\","
                + "\"password\": \"password4\","
                + "\"phoneNumber\": \"323456789\","
                + "\"active\": true"
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
    void shouldNotCreateCustomerIfEmpty() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateCustomerIfValidInput() throws Exception {
        Customer updatedCustomer = new Customer(2L, "john", "UpdatedTest2@example.com", "UpdatedPassword2", "Updated123456789", true);
        when(customerService.updateCustomer(eq(2L), any(Customer.class))).thenReturn(updatedCustomer);

        String json = "{"
                + "\"customerId\": 2,"
                + "\"name\": \"john\","
                + "\"email\": \"UpdatedTest2@example.com\","
                + "\"password\": \"UpdatedPassword2\","
                + "\"phoneNumber\": \"Updated123456789\","
                + "\"active\": true"
                + "}";

        mockMvc.perform(put("/api/customers/2")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteCustomerIfCustomerEmailValid() throws Exception {
        doNothing().when(customerService).deleteCustomer("test3@example.com", "password3");

        mockMvc.perform(delete("/api/customers/test3@example.com")
                        .param("password", "password3"))
                .andExpect(status().isNoContent());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteCustomerIfPasswordValid() throws Exception {
        doNothing().when(customerService).deleteCustomer("test3@example.com", "password3");

        mockMvc.perform(delete("/api/customers/test3@example.com")
                        .param("password", "password3"))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer("test3@example.com", "password3");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotDeleteCustomerIfPasswordInvalid() throws Exception {
        doThrow(new InvalidPasswordException()).when(customerService).deleteCustomer("test3@example.com", "wrongPassword");

        mockMvc.perform(delete("/api/customers/test3@example.com")
                        .param("password", "wrongPassword"))
                .andExpect(status().isUnauthorized());

        verify(customerService).deleteCustomer("test3@example.com", "wrongPassword");
    }

}
