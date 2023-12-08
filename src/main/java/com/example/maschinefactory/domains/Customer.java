package com.example.maschinefactory.domains;

import jakarta.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

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

    @Column(name = "number")
    private String number; // Ensure this is the correct data type

    @Column(name = "active")
    private boolean active;

    // No-arg constructor
    public Customer() {
        // JPA requires a no-arg constructor
    }

    /*
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "customer_address",
        joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "addressId")
    )
    private List<Address> addresses;
    */

    public Customer(long userId, String name, String email, String password, String number, boolean active) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.number = number;
        this.active = active;
    }

    public Customer(String name, String email, String password, String number, boolean active) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.number = number;
        this.active = active;
    }


    // Getters and Setters

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /*public List<Order> getOrders() {
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
    */

    @Override
    public String toString() {
        return "Customer [userId=" + userId + ", name=" + name + ", email=" + email + ", password=" + password + ", number="
                + number + ", active=" + active + "]";
    }
}
