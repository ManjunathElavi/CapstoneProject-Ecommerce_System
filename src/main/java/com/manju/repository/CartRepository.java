package com.manju.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manju.entity.Cart;
import com.manju.entity.Customer;

public interface CartRepository extends JpaRepository<Cart, Integer> {
	Optional<Cart> findByCustomer(Customer customer);

}
