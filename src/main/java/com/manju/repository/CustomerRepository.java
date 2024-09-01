package com.manju.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manju.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    
}
