package com.example.security.services;

import com.example.security.entities.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> findAllCustomers();

    Customer findCustomerById(Long id);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    void deleteCustomer(Long id);

}
