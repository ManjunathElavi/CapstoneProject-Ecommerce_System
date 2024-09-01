package com.manju.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.manju.entity.Cart;
import com.manju.entity.CartItem;
import com.manju.entity.Customer;
import com.manju.entity.Order;
import com.manju.entity.Product;
import com.manju.exception.ResourceNotFoundException;
import com.manju.repository.CartRepository;
import com.manju.repository.CustomerRepository;
import com.manju.repository.OrderRepository;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        int customerId = 1;
        int cartId = 1;
        String shippingAddress = "123 Test St";

        Customer customer = new Customer();
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setPrice(100.0);
        cartItem.setQuantity(1);
        cartItem.setProduct(new Product());
        cart.getItems().add(cartItem);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order createdOrder = orderService.createOrder(customerId, cartId, shippingAddress);

        assertNotNull(createdOrder);
        assertEquals(customer, createdOrder.getCustomer());
        assertEquals(cart, createdOrder.getCart());
        assertEquals(shippingAddress, createdOrder.getShippingAddress());
        assertEquals(100.0, createdOrder.getTotal());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
    
    
    @Test
    void testGetOrderById() {
        int orderId = 1;
        Order order = new Order();
        order.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(orderId);

        assertNotNull(foundOrder);
        assertEquals(orderId, foundOrder.getOrderId());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetOrderById_NotFound() {
        int orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(orderId));
        verify(orderRepository, times(1)).findById(orderId);
    }
 
    @Test
    void testGetOrdersByCustomerId() {
        int customerId = 1;

        // Create a mock order and customer
        Order order = new Order();
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        Customer customer = mock(Customer.class);
        when(customer.getOrders()).thenReturn(orders); // Ensure getOrders() returns the expected list

        // Mock repository call
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Call the service method
        List<Order> resultOrders = orderService.getOrdersByCustomerId(customerId);

        // Assertions
        assertNotNull(resultOrders);
        assertEquals(1, resultOrders.size());
        verify(customerRepository, times(1)).findById(customerId);
    }


    @Test
    void testGetOrdersByCustomerId_NotFound() {
        int customerId = 1;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrdersByCustomerId(customerId));
        verify(customerRepository, times(1)).findById(customerId);
    }
    
    
    @Test
    void testDeleteOrder() {
        int orderId = 1;
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void testDeleteOrder_NotFound() {
        int orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(orderId));
        verify(orderRepository, times(1)).findById(orderId);
    }
    
}
