package com.stackroute.kafkaconsumer.service;

import com.stackroute.kafkaconsumer.domain.Customer;
import com.stackroute.kafkaconsumer.exception.UserAlreadyExistsException;
import com.stackroute.kafkaconsumer.exception.UserNotFoundException;

import java.util.List;

public interface CustomerService {
     public List<Customer> getAllCustomers();

     public Customer getCustomerByEmail(String email) throws UserNotFoundException;

     public Customer saveNewCustomer(Customer customer) throws UserAlreadyExistsException;

     public void deleteCustomerByEmail(String email) throws UserNotFoundException;

     public Customer updateCustomer(Customer customer);
}
