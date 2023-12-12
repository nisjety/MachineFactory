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



    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateNewAddressIfAddressValid() throws Exception {
        Address newAddress = new Address(4L,"4 Main St", "Randomcity 4",5432, "Sverige");
        when(addressService.createAddress(any(Address.class))).thenReturn(newAddress);

        String json = "{"
                + "\"addressId\": 4,"
                + "\"street\": \"4 Main st\","
                + "\"city\": \"Randomcity 4\","
                + "\"zip\": \"5432\","
                + "\"country\": \"Sverige 4\""
                + "}";

        mockMvc.perform(post("/api/addresses")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotCreateAddressIfAddressInValid() throws Exception {
        String json = """
            {
                "addressId":"",
                "street": "main st 4",
                "city": "randomcity 4",
                "zip": "",
                "country": "true"
            }
            """;
        mockMvc.perform(post("/api/addresses")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotCreateAddressIfEmpty() throws Exception {
        mockMvc.perform(post("/api/addresses")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateAddressIfValidInput() throws Exception {
        Address existingAddress = new Address(3L, "3 Main St", "Randomcity3", 9876, "Italia");
        Address updatedAddress = new Address(3L, "Updated 3 Main_St", "Updated Randomcity3", 9876, "Italia");
        when(addressService.updateAddress(eq(3L), eq("Updated 3 Main_St"), eq("Updated Randomcity3"), eq(9876), eq("Italia")))
                .thenReturn(updatedAddress);

        String json = "{"
                + "\"addressId\": 3,"
                + "\"street\": \"Updated 3 Main_St\","
                + "\"city\": \"Updated Randomcity3\","
                + "\"zip\": 9876,"
                + "\"country\": \"Italia\""
                + "}";

        mockMvc.perform(put("/api/addresses/3")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isAccepted());
    }



        @Test
    @WithMockUser
    void shouldGetCustomerFromAddress() throws Exception {
        List<Customer> customers = List.of( new Customer(1L, "ole", "test1@example.com", "password", "023456789", true));
        when(addressService.getCustomersFromAddress(1L)).thenReturn(customers);

        mockMvc.perform(get("/api/addresses/1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(customers.size())));
    }





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



    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddCustomerToAddress() throws Exception {
        Customer customer =  new Customer(1L, "ole", "test1@example.com", "password", "023456789", true);

        Address address = new Address(1L,"1 Main_St", "Randomcity1",1234, "USA");
        when(addressService.addCustomerToAddress(eq(1L), any(Customer.class))).thenReturn(address);

        String customerJson ="""
    {
        "customerId": 1,
        "name": "ole",
        "email": "test1@example.com",
        "password": "password",
        "phoneNumber": "023456789",
        "active": true
    }
    """;
        mockMvc.perform(put("/api/addresses/1/customers")
                        .contentType("application/json")
                        .content(customerJson))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser
    void shouldRemoveOrderFromAddress() throws Exception {
        doNothing().when(addressService).removeOrderFromAddress(1L, 1L);

        mockMvc.perform(delete("/api/addresses/1/orders/1"))
                .andExpect(status().isNoContent());

        verify(addressService).removeOrderFromAddress(1L, 1L);
    }

    @Test
    @WithMockUser
    void shouldRemoveCustomerFromAddress() throws Exception {
        doNothing().when(addressService).removeCustomerFromAddress(2L, 1L);

        mockMvc.perform(delete("/api/addresses/2/customers/1"))
                .andExpect(status().isNoContent());

        verify(addressService).removeCustomerFromAddress(2L, 1L);
    }
}
