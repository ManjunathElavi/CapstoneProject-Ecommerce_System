package com.manju.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manju.entity.Cart;
import com.manju.entity.CartItem;
import com.manju.entity.Customer;
import com.manju.entity.Order;
import com.manju.entity.OrderItem;
import com.manju.exception.ResourceNotFoundException;
import com.manju.repository.CartRepository;
import com.manju.repository.CustomerRepository;
import com.manju.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService implements IOrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private CartRepository cartRepository;

    @Transactional
    public Order createOrder(int customerId, int cartId, String shippingAddress) {
    	
    
    	Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));
    	
    	
    	Optional<Order> existingOrder = orderRepository.findByCart(cart);
        if (existingOrder.isPresent()) {
            throw new ResourceNotFoundException("A order already exists for the cart with id: " + customerId);
        }
    	 
        Customer customer = customerRepository.findById(customerId)
              .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        

        Order order = new Order();
        order.setCustomer(customer);
        order.setCart(cart);
        order.setShippingAddress(shippingAddress);
        
        // Calculate the total order price from cart items
        double total = 0;
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            order.addOrderItem(orderItem); // Add the order item to the order
            
            total += cartItem.getPrice();
        }
        order.setTotal(total);

        return orderRepository.save(order);
    }

    @Transactional()
    public Order  getOrderById(int orderId) {
    	 Order order = orderRepository.findById(orderId)
                 .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + orderId));

    	 Hibernate.initialize(order.getCustomer());
    	    Hibernate.initialize(order.getOrderItems());
    	    Hibernate.initialize(order.getCart());
    	    
    	    Cart cart = order.getCart();
    	    if (cart != null) {
    	        Hibernate.initialize(cart.getItems());
    	        for (CartItem item : cart.getItems()) {
    	            Hibernate.initialize(item.getProduct());
    	        }
    	    }

         return order;
    }

    public List<Order> getOrdersByCustomerId(int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Hibernate.initialize(customer.getOrders()); 
        return customer.getOrders();
    }

    public void deleteOrder(int orderId) {
    	
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        orderRepository.delete(order);
    }
}
