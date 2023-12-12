package com.example.maschinefactory;

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

@WebMvcTest(AddressController.class)
@Import(SecurityConfig.class)
public class AddressServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    private List<Address> addresses;

    private List<Customer> customers;

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
    @WithMockUser
    void shouldFindAllAddress() throws Exception {
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);
        Page<Address> pagedResponse = new PageImpl<>(addresses.subList(0, size), pageable, addresses.size());

        when(addressService.findAllAddress(pageable)).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/addresses")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.totalElements", is(addresses.size())));
    }

    @Test
    @WithMockUser
    void shouldFindAddressBasedOnId() throws Exception {
        when(addressService.findAddressById(1L)).thenReturn(Optional.of(addresses.get(0)));

        var address = addresses.get(0);
        var json = "{"
                + "\"addressId\":" + address.getAddressId() + ","
                + "\"street\":\"" + address.getStreet() + "\","
                + "\"city\":\"" + address.getCity() + "\","
                + "\"zip\":" + address.getZip() + ","
                + "\"country\":\"" + address.getCountry() + "\""
                + "}";

        mockMvc.perform(get("/api/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @WithMockUser
    void shouldGetAddressNotExists() throws Exception {
        when(addressService.findAddressById(99L)).thenThrow(AddressNotFoundException.class);
        mockMvc.perform(get("/api/addresses/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testFindAddressByStreet() {
        Page<Address> pagedResponse = new PageImpl<>(addresses);
        when(addressService.findAddressByStreet(anyString(), any(Pageable.class))).thenReturn(pagedResponse);

        Page<Address> result = addressService.findAddressByStreet("1 Main_St", PageRequest.of(0, 10));

        assertThat(result.getContent()).isEqualTo(addresses);
    }
    @Test
    @WithMockUser
    void testFindAddressByCity() {
        Page<Address> pagedResponse = new PageImpl<>(addresses);
        when(addressService.findAddressByCity(anyString(), any(Pageable.class))).thenReturn(pagedResponse);

        Page<Address> result = addressService.findAddressByCity("Randomcity2", PageRequest.of(0, 10));

        assertThat(result.getContent()).isEqualTo(addresses);
    }


    @Test
    @WithMockUser
    void testFindAddressByZip() {
        Page<Address> pagedResponse = new PageImpl<>(addresses);
        when(addressService.findAddressByZip(anyInt(), any(Pageable.class))).thenReturn(pagedResponse);

        Page<Address> result = addressService.findAddressByZip(9876, PageRequest.of(0, 10));

        assertThat(result.getContent()).isEqualTo(addresses);
    }

    @Test
    @WithMockUser
    void testFindAddressByCountry() {
        Optional<Address> expectedAddress = Optional.of(addresses.get(0));
        when(addressService.findAddressByCountry(anyString())).thenReturn(expectedAddress);

        Optional<Address> result = addressService.findAddressByCountry("Italia");

        assertThat(result).isEqualTo(expectedAddress);
    }

    @Test
    @WithMockUser
    void testFindAddressByStreetAndCity() {
        // Create a page of addresses for testing
        Page<Address> pagedResponse = new PageImpl<>(addresses);

        // Given: Mock the repository call in the service
        when(addressService.findAddressByStreetAndCity(eq("1 Main St"), eq("Randomcity2"), any(Pageable.class)))
                .thenReturn(pagedResponse);

        // When: Call the service method
        Page<Address> result = addressService.findAddressByStreetAndCity("1 Main St", "Randomcity2", PageRequest.of(0, 10));

        // Then: Assert that the result matches the expected response
        assertThat(result.getContent()).hasSameElementsAs(addresses);

        // Optionally, verify the interaction with the mocked service
        verify(addressService).findAddressByStreetAndCity(eq("1 Main St"), eq("Randomcity2"), any(Pageable.class));
    }

    @Test
    @WithMockUser
    void testFindAddressByStreetAndZip() {
        Page<Address> pagedResponse = new PageImpl<>(addresses);
        when(addressService.findAddressByStreetAndZip(eq("Main St"), eq(1234), any(Pageable.class)))
                .thenReturn(pagedResponse);

        Page<Address> result = addressService.findAddressByStreetAndZip("Main St", 1234, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSameElementsAs(addresses);
    }

    @Test
    @WithMockUser
    void testFindAddressByStreetAndCityAndZip() {
        Page<Address> pagedResponse = new PageImpl<>(addresses);
        when(addressService.findAddressByStreetAndCityAndZip(eq("Main St"), eq("Randomcity2"), eq(5678), any(Pageable.class)))
                .thenReturn(pagedResponse);

        Page<Address> result = addressService.findAddressByStreetAndCityAndZip("Main St", "Randomcity2", 5678, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSameElementsAs(addresses);
    }

    @Test
    @WithMockUser
    void testFindAddressByCityAndZip() {
        Page<Address> pagedResponse = new PageImpl<>(addresses);
        when(addressService.findAddressByCityAndZip(eq("Randomcity3"), eq(9876), any(Pageable.class)))
                .thenReturn(pagedResponse);

        Page<Address> result = addressService.findAddressByCityAndZip("Randomcity3", 9876, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSameElementsAs(addresses);
    }

    @Test
    @WithMockUser
    void testFindByCountryAndZip() {
        Address expectedAddress = addresses.get(0);
        when(addressService.findByCountryAndZip(eq("USA"), eq(1234)))
                .thenReturn(Optional.of(expectedAddress));

        Optional<Address> result = addressService.findByCountryAndZip("USA", 1234);

        assertThat(result).contains(expectedAddress);
    }

    @Test
    @WithMockUser
    void testFindAddressByCountryAndCity() {
        Address expectedAddress = addresses.get(1);
        when(addressService.findAddressByCountryAndCity(eq("Norway"), eq("Randomcity2")))
                .thenReturn(Optional.of(expectedAddress));

        Optional<Address> result = addressService.findAddressByCountryAndCity("Norway", "Randomcity2");

        assertThat(result).contains(expectedAddress);
    }

    /*

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateNewCustomerIfCustomerValid() throws Exception {
        Customer newCustomer = new Customer(4L, "bole", "test4@example.com", "password4", "323456789", true);
        when(addressService.createCustomer(any(Customer.class))).thenReturn(newCustomer);

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
        doThrow(new InvalidPasswordException()).when(customerService).deleteCustomer("test3@example.com", "wrongPassword");

        mockMvc.perform(delete("/api/customers/test3@example.com")
                        .param("password", "wrongPassword"))
                .andExpect(status().isUnauthorized());

        verify(customerService).deleteCustomer("test3@example.com", "wrongPassword");
    }

    @Test
    @WithMockUser
    void shouldGetAddressesForCustomer() throws Exception {
        List<Address> addresses = List.of( new Address(1L,1234, "Randomcity1","1 Main_St", "USA"));
        when(customerService.getAddressForCustomer(1L)).thenReturn(addresses);

        mockMvc.perform(get("/api/customers/1/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(addresses.size())));
    }

     */




    /*
    @Test
    @WithMockUser
    void shouldGetOrdersForCustomer() throws Exception {
        List<Order> orders = List.of(new Order(order details));
        when(customerService.getOrdersForCustomer(1L)).thenReturn(orders);

        mockMvc.perform(get("/api/customers/1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(orders.size())));
    }


    @Test
    @WithMockUser
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




    /*
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddAddressToCustomer() throws Exception {
        Address address =  new Address(1L,1234, "Randomcity1","1 Main_St", "USA");

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
    @WithMockUser
    void shouldRemoveOrderFromCustomer() throws Exception {
        doNothing().when(customerService).removeOrderFromCustomer(1L, 1L);

        mockMvc.perform(delete("/api/customers/1/orders/1"))
                .andExpect(status().isNoContent());

        verify(customerService).removeOrderFromCustomer(1L, 1L);
    }

    @Test
    @WithMockUser
    void shouldRemoveAddressFromCustomer() throws Exception {
        doNothing().when(customerService).removeAddressFromCustomer(1L, 1L);

        mockMvc.perform(delete("/api/customers/1/addresses/1"))
                .andExpect(status().isNoContent());

        verify(customerService).removeAddressFromCustomer(1L, 1L);
    }
*/


}
