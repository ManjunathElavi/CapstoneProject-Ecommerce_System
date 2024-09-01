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

import com.manju.entity.Customer;
import com.manju.exception.ResourceNotFoundException;
import com.manju.repository.CustomerRepository;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCustomer() {
    	
        Customer customer = new Customer();
        customer.setName("John Doe");
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.addCustomer(customer);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testUpdateCustomer_CustomerExists() {
    	
        int customerId = 1;
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(customerId);
        existingCustomer.setName("John Doe");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("Jane Doe");
        updatedCustomer.setEmail("jane@example.com");
        updatedCustomer.setPhoneNumber("1234567890");
        updatedCustomer.setPassword("newPassword");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(existingCustomer);

        Customer result = customerService.updateCustomer(customerId, updatedCustomer);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane@example.com", result.getEmail());
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    public void testUpdateCustomer_CustomerNotFound() {
    	
        int customerId = 1;
        Customer updatedCustomer = new Customer();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(customerId, updatedCustomer));
    }

    @Test
    public void testGetCustomerById_CustomerExists() {
    	
        int customerId = 1;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(customerId);

        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    public void testGetCustomerById_CustomerNotFound() {
  
        int customerId = 1;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

     
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(customerId));
    }

    @Test
    public void testGetAllCustomers() {
        
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

     
        List<Customer> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteCustomer_CustomerExists() {
     
        int customerId = 1;
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(customerId);

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    public void testDeleteCustomer_CustomerNotFound() {

        int customerId = 1;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(customerId));
    }
}
