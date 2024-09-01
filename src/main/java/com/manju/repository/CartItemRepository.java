package com.manju.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manju.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
   
}
