package com.stackroute.kafkaconsumer.service;


import com.stackroute.kafkaconsumer.domain.Customer;
import com.stackroute.kafkaconsumer.exception.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;


/**
 * Consumer class should be used to receive messages from Kafka Topic
 * Annotate this class to create a bean of type "Service"
 */

@Service
public class Consumer {

    /**
     * Create a slf4j Logger for logging messages to standard output
     */

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);


    @Autowired
    private CustomerService customerService;

    private final CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }

    /**
     * Create a method receive(Customer message)
     * to receive order details message from Kafka topic
     * Use KafkaListener annotation to achieve the same
     * On receipt of the message, count down the latch value(useful for testing receipt of message)
     * Update the order in the customer Document in MongoDB using the customerService
     * Log error messages in case of failure, log informational with message details
     * when message is successfully received
     * Get Total number of customer using customerService getAllCustomers method increment that value by 1
     * and assign that value as received customer message id parameter's value
     */
    @KafkaListener(topics = "customer_details",  containerFactory = "kafkaListenerContainerFactory")
    public Customer consume(Customer message) throws UserAlreadyExistsException {
        latch.countDown();
        int length = customerService.getAllCustomers().toArray().length;
        message.setId("" + length+1);
        LOGGER.info(String.format("$$ -> Consumed Message -> %s",message.toString()));
        customerService.saveNewCustomer(message);
        return message;
    }
}
