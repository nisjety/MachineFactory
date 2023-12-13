package com.example.maschinefactory.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import com.example.maschinefactory.customer.Customer;
import com.example.maschinefactory.address.Address;
import com.example.maschinefactory.machine.Machine;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @DateTimeFormat
    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "status")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressId")
    private Address deliveryAddress;

  /*  @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_machine",
            joinColumns = @JoinColumn(name = "orderId"),
            inverseJoinColumns = @JoinColumn(name = "machineId")
    )
    private List<Machine> machines;
*/
    // No-arg constructor for JPA
    public Order() {
    }

    public Order(long orderId, Date orderDate, Status status) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.status = status;
    }

    // Getters and Setters

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Getters and Setters for new fields
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
/*
    public List<Machine> getMachines() {
        return machines;
    }

    public void setMachines(List<Machine> machines) {
        this.machines = machines;
    }
   */
    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", orderDate=" + orderDate + ", status=" + status + "]";
    }
}
