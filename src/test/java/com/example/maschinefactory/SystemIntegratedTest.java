package com.example.maschinefactory;

import com.example.maschinefactory.address.*;
import com.example.maschinefactory.customer.*;
import com.example.maschinefactory.order.OrderEntity;
import com.example.maschinefactory.part.*;
import com.example.maschinefactory.security.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Transactional
public class SystemIntegratedTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PartRepository partRepository;

    @BeforeEach
    void setUp() {
        // Create and hash the password
        String hashedPassword = new BCryptPasswordEncoder().encode("password");

        // Create a new customer
        Customer customer = new Customer(1L, "ole", "test1@example.com", hashedPassword, "023456789", true);

        // Create a new address
        Address address = new Address(1L, "1 Main St", "Randomcity1", 1234, "USA");


        // Check if the address's customer list is initialized, if not, initialize it
        if (address.getCustomers() == null) {
            address.setCustomers(new ArrayList<>());
        }
        if (customer.getAddresses() == null) {
            customer.setAddresses(new ArrayList<>());
        }

        // Add the customer to the address
        address.getCustomers().add(customer);
        customer.getAddresses().add(address);

        // Save the customer and address to the repository
        customer = customerRepository.save(customer);
        address = addressRepository.save(address);
    }

    @AfterEach
    void cleanUp() {
        addressRepository.deleteAll();
        customerRepository.deleteAll();
        partRepository.deleteAll();
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

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldAddCustomerToAddress() throws Exception {
        Address address = new Address(1L,"1 Main St", "Randomcity1",1234, "USA");
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
    public void shouldAddAddressToCustomer() throws Exception {
        Customer customer = new Customer(1L, "ole", "test1@example.com", "password", "023456789", true);
        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(customer);

        //adding customer to address 1
        mockMvc.perform(put("/api/addresses/1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldAddCustomerToOrder() throws Exception {
        OrderEntity order = new OrderEntity();
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


}
