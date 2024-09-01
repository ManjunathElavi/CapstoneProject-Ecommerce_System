package com.manju.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manju.entity.Cart;
import com.manju.service.CartService;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;
    

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable int id) {
        return new ResponseEntity<>(cartService.getCartById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Cart> createCart(@RequestParam int customerId) {
        Cart cart = cartService.createCart(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable int id) {
        cartService.deleteCart(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{cartId}/addProduct")
    public ResponseEntity<Cart> addProductToCart(@PathVariable int cartId,
    		                                     @RequestParam  int productId,
                                                 @RequestParam int quantity) {
        return new ResponseEntity<>(cartService.addProductToCart(cartId, productId, quantity), HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}/removeProduct")
    public ResponseEntity<Cart> removeProductFromCart(@PathVariable int cartId,
                                                      @RequestParam int productId,
                                                      @RequestParam int quantity) {
        return new ResponseEntity<>(cartService.removeProductFromCart(cartId, productId, quantity), HttpStatus.OK);
    }

}

