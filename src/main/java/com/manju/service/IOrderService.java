package com.manju.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.manju.entity.Order;

@Service
public interface IOrderService {

    Order createOrder(int customerId, int cartId, String shippingAddress);

    List<Order> getOrdersByCustomerId(int customerId);

    void deleteOrder(int orderId);
}
