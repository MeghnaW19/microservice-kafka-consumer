package com.stackroute.kafkaconsumer.repository;

import com.stackroute.kafkaconsumer.domain.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer,String> {

    public Optional<Customer> findByEmail(String email);
}
