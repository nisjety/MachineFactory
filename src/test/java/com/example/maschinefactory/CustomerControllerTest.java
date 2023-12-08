package com.example.maschinefactory;

import com.example.maschinefactory.controllers.CustomerController;
import com.example.maschinefactory.domains.Customer;
import com.example.maschinefactory.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;


    List<Customer> customers=new ArrayList<>();

    @BeforeEach
    void setUp() {
        customers = List.of(
                new Customer(1, "ole", "test1@example.com", "password",  "023456789", true),
                new Customer(2, "john", "test2@example.com", "password2", "123456789", true),
                new Customer(3, "karma", "test3@example.com", "password3", "2234567890", false)
        );
    }

    @Test
    void shouldFindAllUsers() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk());
    }
}

