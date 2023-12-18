package com.example.maschinefactory.order;

import com.example.maschinefactory.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public interface OrderController {
    @PostMapping("")
    ResponseEntity<ApiResponse> createOrder(@RequestBody OrderEntity orderEntity);
    @PutMapping("/{orderId}")
    ResponseEntity<ApiResponse> updateOrder(@PathVariable("orderId") Long orderId, @RequestBody OrderEntity orderEntity);
    @GetMapping("")
    ResponseEntity<List<OrderEntity>> getAllOrders(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size);
    @GetMapping("/{orderId}")
    ResponseEntity<OrderEntity> getOrderById(@PathVariable("orderId") Long orderId);
    @DeleteMapping("/{orderId}")
    ResponseEntity<ApiResponse> deleteOrderById(@PathVariable("orderId") Long orderId);
}
