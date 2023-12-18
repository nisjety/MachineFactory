package com.example.maschinefactory.customerTest;

import com.example.maschinefactory.address.*;
import com.example.maschinefactory.customer.*;
import com.example.maschinefactory.order.Order;
import com.example.maschinefactory.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
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

    private List<Address> addresses;

    @BeforeEach
    void setUp() {
        customers = List.of(
                new Customer(1L, "ole", "test1@example.com", "password", "023456789", true),
                new Customer(2L, "john", "test2@example.com", "password2", "123456789", true),
                new Customer(3L, "karma", "test3@example.com", "password3", "223456789", false)
        );

        addresses = List.of(
                new Address(1L,"1 Main_St", "Randomcity1",1234, "USA"),
                new Address(2L,"2 Main_St", "Randomcity2",5679, "Norway"),
                new Address(3L,"3 Main_St", "Randomcity3",9876, "Italia")

        );
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindAllCustomers() throws Exception {
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> pagedResponse = new PageImpl<>(customers.subList(0, size), pageable, customers.size());

        when(customerService.findAllCustomers(pageable)).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/customers")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.totalElements", is(customers.size())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
    void shouldGetCustomerNotExists() throws Exception {
        when(customerService.findCustomerById(99L)).thenThrow(CustomerNotFoundException.class);
        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindCustomersByName() {
        Page<Customer> pagedResponse = new PageImpl<>(customers);
        when(customerService.findCustomersByName(anyString(), any(Pageable.class))).thenReturn(pagedResponse);

        Page<Customer> result = customerService.findCustomersByName("testName", PageRequest.of(0, 10));

        assertThat(result.getContent()).isEqualTo(customers);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindCustomerByEmail() {
        Optional<Customer> expectedCustomer = Optional.of(customers.get(0));
        when(customerService.findCustomerByEmail(anyString())).thenReturn(expectedCustomer);

        Optional<Customer> result = customerService.findCustomerByEmail("test1@example.com");

        assertThat(result).isEqualTo(expectedCustomer);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindCustomersByActiveStatus() {
        Page<Customer> pagedResponse = new PageImpl<>(customers);
        when(customerService.findCustomersByActiveStatus(anyBoolean(), any(Pageable.class))).thenReturn(pagedResponse);

        Page<Customer> result = customerService.findCustomersByActiveStatus(true, PageRequest.of(0, 10));

        assertThat(result.getContent()).isEqualTo(customers);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindCustomersByPhoneNumber() {
        Optional<Customer> expectedCustomer = Optional.of(customers.get(0));
        when(customerService.findCustomersByPhoneNumber(anyString())).thenReturn(expectedCustomer);

        Optional<Customer> result = customerService.findCustomersByPhoneNumber("023456789");

        assertThat(result).isEqualTo(expectedCustomer);
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
                .andExpect(status().isAccepted());
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
        doThrow(new InvalidPasswordException("wrong password")).when(customerService).deleteCustomer("test3@example.com", "wrongPassword");

        mockMvc.perform(delete("/api/customers/test3@example.com")
                        .param("password", "wrongPassword"))
                .andExpect(status().isUnauthorized());

        verify(customerService).deleteCustomer("test3@example.com", "wrongPassword");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAddressesForCustomer() throws Exception {
        List<Address> addresses = List.of( new Address(1L,"1 Main_St", "Randomcity1",1234, "USA"));
        when(customerService.getAddressForCustomer(1L)).thenReturn(addresses);

        mockMvc.perform(get("/api/customers/1/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(addresses.size())));
    }
/*
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetOrdersForCustomer() throws Exception {
        List<Order> orders = List.of(new Order(order details));
        when(customerService.getOrdersForCustomer(1L)).thenReturn(orders);

        mockMvc.perform(get("/api/customers/1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(orders.size())));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddOrderToCustomer() throws Exception {
        Order order = new Order(order details );
        Customer customer = new Customer(customer details);
        when(customerService.addOrderToCustomer(eq(1L), any(Order.class))).thenReturn(customer);

        String orderJson = JSON representation of order ;
        mockMvc.perform(put("/api/customers/1/orders")
                        .contentType("application/json")
                        .content(orderJson))
                .andExpect(status().isAccepted());
    }
    */

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddAddressToCustomer() throws Exception {
        Address address =  new Address(1L,"1 Main_St", "Randomcity1",1234, "USA");

        Customer customer = new Customer(2L, "john", "test2@example.com", "password2", "123456789", true);
        when(customerService.addAddressToCustomer(eq(1L), any(Address.class))).thenReturn(customer);

        String addressJson ="""
    {
        "addressId": 1,
        "street": "123 Main St",
        "city": "Anytown",
        "zipCode": "12345",
        "country": "USA"
    }
    """;
        mockMvc.perform(put("/api/customers/1/addresses")
                        .contentType("application/json")
                        .content(addressJson))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRemoveOrderFromCustomer() throws Exception {
        doNothing().when(customerService).removeOrderFromCustomer(1L, 1L);

        mockMvc.perform(delete("/api/customers/1/orders/1"))
                .andExpect(status().isNoContent());

        verify(customerService).removeOrderFromCustomer(1L, 1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRemoveAddressFromCustomer() throws Exception {
        doNothing().when(customerService).removeAddressFromCustomer(1L, 1L);

        mockMvc.perform(delete("/api/customers/1/addresses/1"))
                .andExpect(status().isNoContent());

        verify(customerService).removeAddressFromCustomer(1L, 1L);
    }
}
