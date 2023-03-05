package com.example.security.controllers;

import com.example.security.dtos.CustomerDTO;
import com.example.security.entities.Customer;
import com.example.security.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final ModelMapper modelMapper;
    private final CustomerService customerService;

    @Autowired
    public CustomerController(ModelMapper modelMapper, CustomerService customerService) {
        this.modelMapper = modelMapper;
        this.customerService = customerService;
    }


    @GetMapping("/findAll")
    public List<CustomerDTO> getAllCustomers() {
        return customerService.findAllCustomers().
                parallelStream().map(customer ->
                        modelMapper.map(customer, CustomerDTO.class)).toList();
    }

    @GetMapping("/findById")
    public CustomerDTO findCustomerById(@PathVariable Long id) {
        return modelMapper.map(customerService.findCustomerById(id), CustomerDTO.class);
    }

    @PostMapping("/create")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        return modelMapper.map(
                customerService.updateCustomer(modelMapper.map(customerDTO, Customer.class)), CustomerDTO.class);
    }

    @PutMapping("/update")
    public CustomerDTO updateCustomer(@RequestBody CustomerDTO customerDTO) {
        return modelMapper.map(
                customerService.updateCustomer(modelMapper.map(customerDTO, Customer.class)), CustomerDTO.class);
    }

    @DeleteMapping("/delete")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

}
