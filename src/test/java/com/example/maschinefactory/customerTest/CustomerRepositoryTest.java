package com.example.maschinefactory.customerTest;

import com.example.maschinefactory.customer.Customer;
import com.example.maschinefactory.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        List<Customer> customers = List.of(
                new Customer(1L, "ole", "test1@example.com", "password", "023456789", true),
                new Customer(2L, "john", "test2@example.com", "password2", "123456789", true),
                new Customer(3L, "karma", "test3@example.com", "password3", "223456789", false));
                customerRepository.saveAll(customers);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnCustomerEmail() throws Exception {
        Optional<Customer> customer = customerRepository.findByEmail("test2@example.com");
        assertThat(customer).isNotNull();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCustomerRepository() {
        Customer newCustomer = new Customer();
        newCustomer.setName("Test User");
        newCustomer.setEmail("test@example.com");
        newCustomer.setPassword("password");
        newCustomer.setPhoneNumber("1234567890");
        newCustomer.setActive(true);

        Customer savedCustomer = customerRepository.save(newCustomer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getName()).isEqualTo("Test User");
        assertThat(savedCustomer.getEmail()).isEqualTo("test@example.com");
    }


}
