package com.example.maschinefactory.order;

import com.example.maschinefactory.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void createOrder(OrderEntity orderEntity) {
        orderRepository.save(orderEntity);
    }

    @Override
    public OrderEntity getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "Id", orderId));
    }

    @Override
    public List<OrderEntity> getAllOrders(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderEntity> customerPage = orderRepository.findAll(pageRequest);
        return customerPage.getContent();
    }

    @Override
    public void updateOrder(Long orderId, OrderEntity updatedOrder) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "Id", orderId));
        orderEntity.setOrderNumber(updatedOrder.getOrderNumber());
        orderRepository.save(orderEntity);
    }

    @Override
    public void deleteOrderById(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}