package com.manju.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.manju.entity.Cart;
import com.manju.entity.CartItem;
import com.manju.entity.Customer;
import com.manju.entity.Product;
import com.manju.exception.ResourceNotFoundException;
import com.manju.repository.CartRepository;
import com.manju.repository.CustomerRepository;
import com.manju.repository.ProductRepository;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CartService cartService;

    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Product product = new Product();
        product.setProductId(1);

       CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(5);

         Cart  cart = new Cart();
        cart.setCartId(1);
        cart.addItem(cartItem);
    }

    @Test
    public void testGetCartById_CartExists() {
        int cartId = 1;
        Cart cart = mock(Cart.class);
        Customer customer = new Customer();
        
        when(cart.getCartId()).thenReturn(cartId);
        when(cart.getCustomer()).thenReturn(customer);
        when(cart.getItems()).thenReturn(new ArrayList<>());
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartById(cartId);

        assertNotNull(result);
        assertEquals(cartId, result.getCartId());
        verify(cartRepository, times(1)).findById(cartId);
        verify(cart, times(1)).getCustomer();  
        verify(cart, times(1)).getItems();     
    }

    @Test
    public void testGetCartById_CartNotFound() {
        int cartId = 1;

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartService.getCartById(cartId));
    }

    @Test
    public void testCreateCart_Success() {
    	
        int customerId = 1;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(cartRepository.findByCustomer(customer)).thenReturn(Optional.empty());

        Cart newCart = new Cart();
        newCart.setCustomer(customer);
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        Cart result = cartService.createCart(customerId);

        assertNotNull(result);
        assertEquals(customer, result.getCustomer());
        verify(customerRepository, times(1)).findById(customerId);
        verify(cartRepository, times(1)).findByCustomer(customer);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testCreateCart_CustomerNotFound() {
        
        int customerId = 1;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartService.createCart(customerId));
    }

    @Test
    public void testCreateCart_CartAlreadyExists() {
      
        int customerId = 1;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(cartRepository.findByCustomer(customer)).thenReturn(Optional.of(new Cart()));

       
        assertThrows(ResourceNotFoundException.class, () -> cartService.createCart(customerId));
    }

    @Test
    public void testDeleteCart_Success() {
    	
        int cartId = 1;
        Cart cart = new Cart();
        cart.setCartId(cartId);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        cartService.deleteCart(cartId);
        
        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, times(1)).delete(cart);
    }

    @Test
    public void testAddProductToCart_ProductNotFound() {
        int cartId = 1;
        int productId = 2;

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(new Cart()));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartService.addProductToCart(cartId, productId, 5));
    }
    
    @Test
    void testAddProductToCart_NewProduct() {
 
        int cartId = 1;
        int productId = 1;
        int quantity = 2;

        Cart cart = new Cart();
        cart.setCartId(cartId);

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(100.0);
        product.setStockQuantity(10);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart updatedCart = cartService.addProductToCart(cartId, productId, quantity);

        assertNotNull(updatedCart);
        assertEquals(1, updatedCart.getItems().size());

        CartItem cartItem = updatedCart.getItems().get(0);
        assertEquals(product, cartItem.getProduct());
        assertEquals(quantity, cartItem.getQuantity());
        assertEquals(product.getPrice() * quantity, cartItem.getPrice(), 0.001);
        assertEquals(8, product.getStockQuantity());
    }
    
    
    @Test
    void testAddProductToCart_ExistingProduct() {
        int cartId = 1;
        int productId = 1;
        int initialQuantity = 2;
        int additionalQuantity = 3;

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(100.0);
        product.setStockQuantity(10);

        CartItem existingItem = new CartItem();
        existingItem.setProduct(product);
        existingItem.setQuantity(initialQuantity);
        existingItem.setPrice(product.getPrice() * initialQuantity);

        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.addItem(existingItem);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart updatedCart = cartService.addProductToCart(cartId, productId, additionalQuantity);

        assertNotNull(updatedCart);
        assertEquals(1, updatedCart.getItems().size());

        CartItem cartItem = updatedCart.getItems().get(0);
        assertEquals(product, cartItem.getProduct());
        assertEquals(initialQuantity + additionalQuantity, cartItem.getQuantity());
        assertEquals(product.getPrice() * cartItem.getQuantity(), cartItem.getPrice(), 0.001);

        assertEquals(7, product.getStockQuantity());
    }


    
    @Test
    void testAddProductToCart_CartNotFound() {
        // Arrange
        int cartId = 999;
        int productId = 1;
        int quantity = 2;

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.addProductToCart(cartId, productId, quantity);
        });

        verify(cartRepository, never()).save(any(Cart.class));
    }





}
