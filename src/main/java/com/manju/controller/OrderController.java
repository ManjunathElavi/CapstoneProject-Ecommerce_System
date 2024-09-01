package com.manju.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.manju.entity.Order;
import com.manju.entity.OrderItem;
import com.manju.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestParam int customerId,
                                             @RequestParam int cartId,
                                             @RequestParam String shippingAddress) {
        Order order = orderService.createOrder(customerId, cartId, shippingAddress);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

   /* @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable int orderId) {
        return new ResponseEntity<>(orderService.getOrderById(orderId), HttpStatus.OK);
    }*/
    
    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable int orderId) {
        return new ResponseEntity<>(orderService.getOrderById(orderId), HttpStatus.OK);
    }
       

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable int customerId) {
        return new ResponseEntity<>(orderService.getOrdersByCustomerId(customerId), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Object> deleteOrder(@PathVariable int orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>("The order with id " +orderId+" is canceled",HttpStatus.OK);
    }
}
