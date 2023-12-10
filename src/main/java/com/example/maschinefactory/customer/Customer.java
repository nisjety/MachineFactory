package com.example.maschinefactory.customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.example.maschinefactory.address.Address;
import com.example.maschinefactory.order.Order;

import java.util.List;

@Entity
@Table(name = "customer")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long customerId;

    @NotBlank(message = "Name cannot be blank")
    @Column(name = "name")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "Phone number cannot be blank")
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "status")
    private boolean active;

    // No-arg constructor for JPA
    public Customer() {
    }

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "customer_address",
        joinColumns = @JoinColumn(name = "customerId"),
        inverseJoinColumns = @JoinColumn(name = "addressId")
    )
    private List<Address> addresses ;

    public Customer(long customerId, String name, String email, String password, String phoneNumber, boolean active) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.active = active;
    }


    // Getters and Setters

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "Customer [customerId=" + customerId + ", name=" + name + ", email=" + email + ", password=" + password + ", phoneNumber="
                + phoneNumber + ", active=" + active + "]";
    }
}
