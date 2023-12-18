package com.example.maschinefactory.impl;

import com.example.maschinefactory.order.OrderControllerImpl;
import com.example.maschinefactory.order.OrderEntity;
import com.example.maschinefactory.order.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderControllerImpl.class)
class OrderControllerImplTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderControllerImpl orderController;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    public OrderControllerImplTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderNumber("123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1.0/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderEntity)))
                .andExpect(status().isCreated());

        verify(orderService, times(1)).createOrder(orderEntity);
    }

    @Test
    void testUpdateOrder() throws Exception {
        Long orderId = 1L;
        OrderEntity updatedOrderEntity = new OrderEntity();
        updatedOrderEntity.setOrderNumber("456");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1.0/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrderEntity)))
                .andExpect(status().isOk());

        verify(orderService, times(1)).updateOrder(anyLong(), any(OrderEntity.class));
    }

    @Test
    void testGetAllOrders() throws Exception {
        int page = 0;
        int size = 10;
        List<OrderEntity> orders = Arrays.asList(new OrderEntity(), new OrderEntity());

        when(orderService.getAllOrders(page, size)).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/orders")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(orders.size()));
    }

    @Test
    void testGetOrderById() throws Exception {
        Long orderId = 1L;
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(orderId);
        orderEntity.setOrderNumber("789");

        when(orderService.getOrderById(orderId)).thenReturn(orderEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderEntity.getOrderId()))
                .andExpect(jsonPath("$.orderNumber").value(orderEntity.getOrderNumber()));
    }

    @Test
    void testDeleteOrderById() throws Exception {
        Long orderId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1.0/orders/{id}", orderId))
                .andExpect(status().isOk());

        verify(orderService, times(1)).deleteOrderById(orderId);
    }
}