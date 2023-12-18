package com.example.maschinefactory.order;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    void createOrder(OrderEntity orderEntity);
    OrderEntity getOrderById(Long orderId);
    List<OrderEntity> getAllOrders(int page, int size);
    void updateOrder(Long orderId, OrderEntity updatedOrder);
    void deleteOrderById(Long orderId);
}
