package com.stackroute.kafkaconsumer.commander.service;

import com.stackroute.kafkaconsumer.domain.Customer;
import com.stackroute.kafkaconsumer.exception.UserAlreadyExistsException;
import com.stackroute.kafkaconsumer.exception.UserNotFoundException;
import com.stackroute.kafkaconsumer.repository.CustomerRepository;
import com.stackroute.kafkaconsumer.service.CustomerServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("1","Nick","Jonas","Male","nick@email.com");
    }

    @AfterEach
    void tearDown() {
        customer = null;
    }

    @Test
    public void givenExistingUserEmailReturnUserDetails() throws UserNotFoundException {
        when(customerRepository.findByEmail("nick@email.com")).thenReturn(Optional.of(customer));
        Customer customerFound = customerService.getCustomerByEmail("nick@email.com");
        assertThat(customerFound).isEqualTo(customer);
    }

    @Test
    public void givenNonExistingUserEmailThenThrowException() throws UserNotFoundException {
        when(customerRepository.findByEmail("jonas@email.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> customerService.getCustomerByEmail("jonas@email.com")).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void givenNonExistingUserThenCreateNewUser() throws UserNotFoundException, UserAlreadyExistsException {
        when(customerRepository.findByEmail("nick@email.com")).thenReturn(Optional.empty());
        when(customerRepository.save(customer)).thenReturn(customer);
        Customer userCreated = customerService.saveNewCustomer(customer);
        assertThat(userCreated).isEqualTo(customer);
    }

    @Test
    public void givenExistingUserThenThrowException() throws UserNotFoundException {
        when(customerRepository.findByEmail("nick@email.com")).thenReturn(Optional.of(customer));
        assertThatThrownBy(() -> customerService.saveNewCustomer(customer)).isInstanceOf(UserAlreadyExistsException.class);

    }

    @Test
    public void givenNonExistingUserEmailToDeleteThenThrowException() throws UserNotFoundException {
        when(customerRepository.findByEmail("nick@email.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> customerService.deleteCustomerByEmail("nick@email.com")).isInstanceOf(UserNotFoundException.class);
    }

}