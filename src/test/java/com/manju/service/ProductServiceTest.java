package com.manju.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.manju.entity.Product;
import com.manju.exception.ResourceNotFoundException;
import com.manju.repository.ProductRepository;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProduct() {
    	
        Product product = new Product();
        product.setName("Laptop");
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.addProduct(product);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testAddProducts() {
       
        Product product1 = new Product();
        product1.setName("Laptop");
        Product product2 = new Product();
        product2.setName("Phone");

        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.saveAll(products)).thenReturn(products);

        
        List<Product> result = productService.addProducts(products);

        
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).saveAll(products);
    }

    @Test
    public void testUpdateProduct_ProductExists() {
        
        int productId = 1;
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setName("Laptop");

        Product updatedProduct = new Product();
        updatedProduct.setName("Laptop Pro");
        updatedProduct.setPrice(1200.00);
        updatedProduct.setStockQuantity(50);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

       
        Product result = productService.updateProduct(productId, updatedProduct);

        
        assertNotNull(result);
        assertEquals("Laptop Pro", result.getName());
        assertEquals(1200.00, result.getPrice());
        assertEquals(50, result.getStockQuantity());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    public void testUpdateProduct_ProductNotFound() {
       
        int productId = 1;
        Product updatedProduct = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

      
        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(productId, updatedProduct));
    }

    @Test
    public void testGetProductById_ProductExists() {
       
        int productId = 1;
        Product product = new Product();
        product.setProductId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

     
        Product result = productService.getProductById(productId);

     
        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testGetProductById_ProductNotFound() {
      
        int productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(productId));
    }

    @Test
    public void testGetAllProducts() {
      
        Product product1 = new Product();
        Product product2 = new Product();
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        
        List<Product> result = productService.getAllProducts();

     
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteProduct_ProductExists() {
       
        int productId = 1;
        Product product = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        
        productService.deleteProduct(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    public void testDeleteProduct_ProductNotFound() {
        
        int productId = 1;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(productId));
    }
}
