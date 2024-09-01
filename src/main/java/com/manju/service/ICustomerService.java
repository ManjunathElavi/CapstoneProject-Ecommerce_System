package com.manju.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.manju.entity.Customer;

@Service
public interface ICustomerService {

    Customer addCustomer(Customer customer);

    Customer updateCustomer(int id, Customer updatedCustomer);

    Customer getCustomerById(int id);

    List<Customer> getAllCustomers();

    void deleteCustomer(int id);
}
