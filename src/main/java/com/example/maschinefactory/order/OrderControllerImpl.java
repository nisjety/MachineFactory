package com.example.maschinefactory.order;

import com.example.maschinefactory.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class OrderControllerImpl implements OrderController {
    private final OrderService orderService;

    public OrderControllerImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<ApiResponse> createOrder(OrderEntity orderEntity) {
        orderService.createOrder(orderEntity);
        return new ResponseEntity<>(new ApiResponse("Order created successfully", true, new Date()), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse> updateOrder(Long orderId, OrderEntity orderEntity) {
        orderService.updateOrder(orderId, orderEntity);
        return new ResponseEntity<>(new ApiResponse("Order updated successfully", true, new Date()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<OrderEntity>> getAllOrders(int page, int size) {
        return new ResponseEntity<>(orderService.getAllOrders(page, size), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderEntity> getOrderById(Long orderId) {
        return new ResponseEntity<>(orderService.getOrderById(orderId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteOrderById(Long orderId) {
        orderService.deleteOrderById(orderId);
        return new ResponseEntity<>(new ApiResponse("Order deleted successfully", true, new Date()), HttpStatus.OK);
    }
}
