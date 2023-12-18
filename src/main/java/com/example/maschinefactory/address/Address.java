package com.example.maschinefactory.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import com.example.maschinefactory.customer.Customer;
import com.example.maschinefactory.order.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotNull
    @Column(name = "Zip")
    @Min(value = 1, message = "Zip code must be greater than 0")
    private Integer zip;

    @NotBlank(message = "Street cannot be blank")
    @Column(name = "Street")
    private String street;

    @NotBlank(message = "city cannot be blank")
    @Column(name = "City")
    private String city;

    @NotBlank(message = "Country number cannot be blank")
    @Column(name = "country")
    private String country;

    @JsonIgnoreProperties("addresses")
    @ManyToMany
    @JoinTable(
            name = "customer_address",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy = "deliveryAddress", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderEntity> orders = new ArrayList<>();


    // No-arg constructor for JPA
    public Address() {
    }

    public Address(Long addressId, String street, String city, int zip, String country) {
        this.addressId = addressId;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.country = country;
    }

    // Getters and Setters

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Address [addressId=" + addressId + ", zip=" + zip + ", street=" + street + ", city=" + city + ", country="
                + country + "]";
    }
}
