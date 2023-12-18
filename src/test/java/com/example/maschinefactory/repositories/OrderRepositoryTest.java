package com.example.maschinefactory.repositories;

import com.example.maschinefactory.order.OrderEntity;
import com.example.maschinefactory.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testSaveAndGetOrder() {
        OrderEntity order = new OrderEntity();
        order.setOrderNumber("12345");
        orderRepository.save(order);
        OrderEntity savedOrder = orderRepository.findById(order.getOrderId()).orElse(null);
        assertNotNull(savedOrder);
        assertEquals("12345", savedOrder.getOrderNumber());
    }

    @Test
    @DirtiesContext
    void testFindAllOrders() {
        OrderEntity order1 = new OrderEntity();
        order1.setOrderNumber("12345");

        OrderEntity order2 = new OrderEntity();
        order2.setOrderNumber("67890");
        orderRepository.saveAll(List.of(order1, order2));
        List<OrderEntity> orders = orderRepository.findAll();
        assertNotNull(orders);
        assertEquals(2, orders.size());
    }
}