package com.stackroute.kafkaconsumer.service;

import com.stackroute.kafkaconsumer.domain.Customer;
import com.stackroute.kafkaconsumer.exception.UserAlreadyExistsException;
import com.stackroute.kafkaconsumer.exception.UserNotFoundException;
import com.stackroute.kafkaconsumer.repository.CustomerRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class to access database repository
 */
@Service
@NoArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * method to get all customer details
     */
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * method to get customer details of given email
     */
    @Override
    public Customer getCustomerByEmail(String email) throws UserNotFoundException {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent())
            return customer.get();
        else
            throw new UserNotFoundException("User with this email " + email + " is not found");
    }

    /**
     * method to save new customer
     */
    @Override
    public Customer saveNewCustomer(Customer customer) throws UserAlreadyExistsException {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent())
            throw new UserAlreadyExistsException("User with email " + customer.getEmail() + " already exists");
        else
            return customerRepository.save(customer);
    }


    /**
     * method to delete a customer of given email
     */
    @Override
    public void deleteCustomerByEmail(String email) throws UserNotFoundException {
        Optional<Customer> existsByEmail = customerRepository.findByEmail(email);
        if (existsByEmail.isPresent())
            customerRepository.deleteById(existsByEmail.get().getEmail());
        else
            throw new UserNotFoundException("User with this email " + email + " is not found");
    }

    /**
     * method to update details of a customer
     */
    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
