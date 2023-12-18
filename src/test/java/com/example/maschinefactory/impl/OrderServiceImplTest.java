package com.example.maschinefactory.impl;

import com.example.maschinefactory.exceptions.ResourceNotFoundException;
import com.example.maschinefactory.order.OrderEntity;
import com.example.maschinefactory.order.OrderRepository;
import com.example.maschinefactory.order.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    public void testCreateOrder() {
        OrderEntity orderEntity = new OrderEntity();
        orderService.createOrder(orderEntity);
        verify(orderRepository, times(1)).save(orderEntity);
    }

    @Test
    public void testGetOrderById_ExistingId() {
        Long orderId = 1L;
        OrderEntity orderEntity = new OrderEntity();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        OrderEntity result = orderService.getOrderById(orderId);
        assertNotNull(result);
        assertEquals(orderEntity, result);
    }

    @Test
    public void testGetOrderById_NonexistentId() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    public void testGetAllOrders() {
        int page = 0;
        int size = 10;
        List<OrderEntity> expectedOrders = Arrays.asList(new OrderEntity(), new OrderEntity());
        PageImpl<OrderEntity> pageResult = new PageImpl<>(expectedOrders);
        when(orderRepository.findAll(any(PageRequest.class))).thenReturn(pageResult);
        List<OrderEntity> result = orderService.getAllOrders(page, size);
        assertNotNull(result);
        assertEquals(expectedOrders.size(), result.size());
    }

    @Test
    public void testUpdateOrder_ExistingId() {
        Long orderId = 1L;
        OrderEntity existingOrder = new OrderEntity();
        OrderEntity updatedOrder = new OrderEntity();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        orderService.updateOrder(orderId, updatedOrder);
        verify(orderRepository, times(1)).save(existingOrder);
        assertEquals(updatedOrder.getOrderNumber(), existingOrder.getOrderNumber());
    }

    @Test
    public void testUpdateOrder_NonexistentId() {
        Long orderId = 1L;
        OrderEntity updatedOrder = new OrderEntity();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(orderId, updatedOrder));
    }

    @Test
    public void testDeleteOrderById() {
        Long orderId = 1L;
        orderService.deleteOrderById(orderId);
        verify(orderRepository, times(1)).deleteById(orderId);
    }
}