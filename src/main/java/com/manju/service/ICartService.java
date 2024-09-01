package com.manju.service;

import org.springframework.stereotype.Service;

import com.manju.entity.Cart;

@Service
public interface ICartService {

    Cart getCartById(int cartId);

    Cart createCart(int customerId);

    void deleteCart(int cartId);

    Cart addProductToCart(int cartId, int productId, int quantity);

    Cart removeProductFromCart(int cartId, int productId, int quantity);
}
