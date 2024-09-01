package com.manju.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manju.entity.Cart;
import com.manju.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	Optional<Order> findByCart(Cart cart);
}
