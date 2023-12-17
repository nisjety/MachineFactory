package com.example.maschinefactory.addressTest;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Transactional
public class AddressIntegratedTest  {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressController addressController;

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

        // Add the customer to the address
        address.getCustomers().add(customer);

        // Save the customer and address to the repository
        customerRepository.save(customer);
        addressRepository.save(address);
    }

    @AfterEach
    void cleanUp() {
        addressRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindAllAddress() throws Exception {
        mockMvc.perform(get("/api/addresses")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].street", is("1 Main St")))
                .andExpect(jsonPath("$.content[1].street", is("2 Main St")))
                .andExpect(jsonPath("$.content[2].street", is("3 Main St")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Rollback
    public void shouldCreateNewAddressIfValid() throws Exception {
        Address address = new Address(4L,"4 Main St", "Randomcity4",12344, "Canada");


        ObjectMapper objectMapper = new ObjectMapper();
        String addressJson = objectMapper.writeValueAsString(address);

        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.street", is("4 Main St")))
                .andExpect(jsonPath("$.zip", is(12344)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldUpdateAddress() throws Exception {
        Address updatedAddress = new Address(1L, "updated 1 Main St", "Randomcity1", 1234, "USA");
        ObjectMapper objectMapper = new ObjectMapper();
        String addressJson = objectMapper.writeValueAsString(updatedAddress);

        mockMvc.perform(put("/api/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.street", is("updated 1 Main St")));
    }
}
