package com.manju.service;

import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manju.entity.Cart;
import com.manju.entity.CartItem;
import com.manju.entity.Customer;
import com.manju.entity.Product;
import com.manju.exception.ResourceNotFoundException;
import com.manju.repository.CartRepository;
import com.manju.repository.CustomerRepository;
import com.manju.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService implements ICartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Transactional()
	public Cart getCartById(int cartId) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));

		Hibernate.initialize(cart.getCustomer());
		Hibernate.initialize(cart.getItems());

		return cart;
	}

	public Cart createCart(int customerId) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

		Optional<Cart> existingCart = cartRepository.findByCustomer(customer);
		if (existingCart.isPresent()) {
			throw new ResourceNotFoundException("A cart already exists for the customer with id: " + customerId);
		}

		Cart cart = new Cart();
		cart.setCustomer(customer);
		return cartRepository.save(cart);
	}

	@Transactional()
	public void deleteCart(int cartId) {
		Cart cart = getCartById(cartId);
		cartRepository.delete(cart);
	}
	
	
	public Cart addProductToCart(int cartId, int productId, int quantity) {
	    Cart cart = getCartById(cartId);
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

	    Optional<CartItem> existingItemOpt = cart.getItems().stream()
	            .filter(item -> item.getProduct().getProductId() == productId).findFirst();

	    if (existingItemOpt.isPresent()) {
	        CartItem existingItem = existingItemOpt.get();
	        int newQuantity = existingItem.getQuantity() + quantity;
	        existingItem.setQuantity(newQuantity);
	        existingItem.setPrice(newQuantity * product.getPrice());
	        product.setStockQuantity(product.getStockQuantity() - quantity); 
	    } else {
	        CartItem cartItem = new CartItem();
	        cartItem.setProduct(product);
	        cartItem.setQuantity(quantity);
	        cartItem.setPrice(product.getPrice() * quantity);
	        product.setStockQuantity(product.getStockQuantity() - quantity);
	        cart.addItem(cartItem);
	    }

	    return cartRepository.save(cart);
	}


	public Cart removeProductFromCart(int cartId, int productId, int quantity) {
		Cart cart = getCartById(cartId);

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		Optional<CartItem> cartItemOpt = cart.getItems().stream()
				.filter(item -> item.getProduct().getProductId() == productId).findFirst();

		if (cartItemOpt.isPresent()) {
			CartItem cartItem = cartItemOpt.get();

			if (cartItem.getQuantity() <= quantity) {
				cart.removeItem(cartItem);
				product.setStockQuantity(product.getStockQuantity() + quantity);
			} else {
				cartItem.setQuantity(cartItem.getQuantity() - quantity);
				product.setStockQuantity(product.getStockQuantity() + quantity);
			}
		} else {
			throw new ResourceNotFoundException("Product not found in cart with id: " + productId);
		}

		return cartRepository.save(cart);
	}

}
