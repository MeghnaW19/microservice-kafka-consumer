package com.stackroute.kafkaconsumer.controller;


import com.stackroute.kafkaconsumer.domain.Customer;
import com.stackroute.kafkaconsumer.exception.UserNotFoundException;
import com.stackroute.kafkaconsumer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to access, modify data in database using REST
 */
@RestController
public class CustomerController {


    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * REST Endpoint for getting a customer of given email
     * URI: /customer  METHOD: GET
     * Response status: success: 201(created)
     */
    @GetMapping("/customer")
    public ResponseEntity getDetailsOfACustomer(@RequestParam("email") String email) throws UserNotFoundException {
        return new ResponseEntity<Customer>(customerService.getCustomerByEmail(email), HttpStatus.CREATED);
    }

    /**
     * REST Endpoint for getting all customer details
     * URI: /customers  METHOD: GET
     * Response status: success: 201(created)
     */
    @GetMapping("/customers")
    public ResponseEntity getAllCustomers() {
        return new ResponseEntity<List<Customer>>(customerService.getAllCustomers(), HttpStatus.CREATED);
    }

    /**
     * REST Endpoint for deleting a customer of given email
     * URI: /delete/customer  METHOD: DELETE
     * Response status: success: 201(created)
     */
    @DeleteMapping("/delete/customer")
    public ResponseEntity getDetailsByEmail(@RequestParam("email") String email) throws UserNotFoundException {
        customerService.deleteCustomerByEmail(email);
        return new ResponseEntity<String>("Deleted successfully", HttpStatus.CREATED);
    }

    /**
     * REST Endpoint for updating details of  a customer
     * URI: /update/customer  METHOD: POST
     * Response status: success: 201(created)
     */
    @PostMapping("/update/customer")
    public ResponseEntity updateCustomers(@RequestBody Customer customer) {

        return new ResponseEntity<Customer>(customerService.updateCustomer(customer), HttpStatus.CREATED);
    }
}
