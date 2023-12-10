package com.example.maschinefactory.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import com.example.maschinefactory.customer.Customer;
import com.example.maschinefactory.order.Order;

import java.util.List;

@Entity
@Table(name = "address")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;

    @NotBlank(message = "Zip code cannot be blank")
    @Column(name = "Zip_Code")
    private int zipCode;


    @NotBlank(message = "Street cannot be blank")
    @Column(name = "street")
    private String street;

    @NotBlank(message = "city cannot be blank")
    @Column(name = "City")
    private String city;

    @NotBlank(message = "Country number cannot be blank")
    @Column(name = "country")
    private String country;

    @ManyToMany(mappedBy = "addresses", fetch = FetchType.LAZY)
    private List<Customer> customers;

    @OneToMany(mappedBy = "deliveryAddress", fetch = FetchType.LAZY)
    private List<Order> orders;


    // No-arg constructor for JPA
    public Address() {
    }

    public Address(long addressId, int zipCode, String street, String city, String country) {
        this.addressId = addressId;
        this.zipCode = zipCode;
        this.street = street;
        this.city = city;
        this.country = country;
    }

    // Getters and Setters

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }


    @Override
    public String toString() {
        return "Address [addressId=" + addressId + ", zipCode=" + zipCode + ", street=" + street + ", city=" + city + ", country="
                + country + "]";
    }
}
