package com.example.maschinefactory.customerTest;

import com.example.maschinefactory.address.*;
import com.example.maschinefactory.customer.*;
import com.example.maschinefactory.security.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import com.example.maschinefactory.customer.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Transactional
public class CustomerIntegratedTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerController customerController;


    @BeforeEach
    void setUp() {
        String hashedPassword = new BCryptPasswordEncoder().encode("password");
        Customer customer = new Customer(1L, "ole", "test1@example.com", hashedPassword, "023456789", true);
        customer = customerRepository.save(customer);

        if (customer.getAddresses() == null) {
            customer.setAddresses(new ArrayList<>());
        }

        Address address = new Address(1L,"1 Main_St", "Randomcity1",1234, "USA");
        customer.getAddresses().add(address);
        customerRepository.save(customer);
    }


    @AfterEach
    void cleanUp() {
        customerRepository.deleteAll();
    }

    @Test
    public void testSerialization() throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customer = new Customer(1L, "ole", "test1@example.com", "password", "023456789", true);
        String json = objectMapper.writeValueAsString(customer);
        System.out.println(json); // This prints the JSON to the console for inspection
        // Additional assertions can be added here to check the JSON structure
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindAllCustomers() throws Exception {
        mockMvc.perform(get("/api/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].name", is("ole")))
                .andExpect(jsonPath("$.content[1].name", is("john")))
                .andExpect(jsonPath("$.content[2].name", is("karma")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Rollback
    public void shouldCreateNewCustomerIfValid() throws Exception {
        Customer customer = new Customer(4L, "bole", "test4@example.com", "Password.4", "4023456789", true);


        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(customer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("bole")))
                .andExpect(jsonPath("$.email", is("test4@example.com")));
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldUpdateCustomer() throws Exception {
        Customer updatedCustomer = new Customer(1L, "Updated Ole", "update@example.com", "UpdatedPassword1$", "1234567890", true);
        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(updatedCustomer);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name", is("Updated Ole")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldDeleteCustomer() throws Exception {
        String customerEmail = "test1@example.com";

        mockMvc.perform(delete("/api/customers/" + customerEmail)
                        .param("password", "password")) // the correct password
                .andExpect(status().isNoContent());
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldAddCustomerToAddress() throws Exception {
        Address address = new Address(4L,"4 Main_St", "Randomcity4",1200, "England");
        ObjectMapper objectMapper = new ObjectMapper();
        String addressJson = objectMapper.writeValueAsString(address);

        //adding address to customer 1
        mockMvc.perform(put("/api/customers/1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldRemoveCustomerFromAddress() throws Exception {
        mockMvc.perform(delete("/api/customers/1/addresses/1"))
                .andExpect(status().isNoContent());
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldUpdateCustomerAddress() throws Exception {
        Address updatedAddress = new Address(1L,"2 Main_St", "Randomcity2",5678, "USA");
        ObjectMapper objectMapper = new ObjectMapper();
        String addressJson = objectMapper.writeValueAsString(updatedAddress);

        mockMvc.perform(put("/api/customers/1/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isOk());
    }


/*
    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldAddCustomerToOrder() throws Exception {
        Order order = new Order();
        ObjectMapper objectMapper = new ObjectMapper();
        String orderJson = objectMapper.writeValueAsString(order);

        mockMvc.perform(put("/api/customers/1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isAccepted());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldRemoveCustomerFromOrder() throws Exception {
        mockMvc.perform(delete("/api/customers/1/orders/1"))
                .andExpect(status().isNoContent());
    }


       @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldUpdateCustomerOrder() throws Exception {
        Order updatedOrder = new Order(appropriate constructor parameters );
    ObjectMapper objectMapper = new ObjectMapper();
    String orderJson = objectMapper.writeValueAsString(updatedOrder);

        mockMvc.perform(put("/api/customers/1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
            .andExpect(status().isAccepted());
}

*/

}
