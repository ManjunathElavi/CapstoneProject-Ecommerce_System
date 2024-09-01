package com.manju.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.manju.entity.Product;

@Service
public interface IProductService {

    Product addProduct(Product product);

    List<Product> addProducts(List<Product> products);

    Product updateProduct(int id, Product updatedProduct);

    Product getProductById(int id);

    List<Product> getAllProducts();

    void deleteProduct(int id);
}
