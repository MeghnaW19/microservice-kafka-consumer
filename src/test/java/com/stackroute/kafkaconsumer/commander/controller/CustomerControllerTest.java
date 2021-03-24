package com.stackroute.kafkaconsumer.commander.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.kafkaconsumer.controller.CustomerController;
import com.stackroute.kafkaconsumer.domain.Customer;
import com.stackroute.kafkaconsumer.service.CustomerService;
import kafka.utils.Json;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private Consumer consumer;

    @Autowired
    private ObjectMapper mapper;


    private Customer customerOne;
    private Customer customerTwo;
    private Customer customerThree;
    private Customer customerFour;

    @BeforeEach
    void setUp() {

        customerOne = new Customer("1","Nick","Jona","Male","nick@email.com");
        customerTwo = new Customer("2","Sarah","Hudson","Female","sara@email.com");
        customerThree = new Customer("3","Kennedy","Buttons","Male","button@email.com");
        customerFour = new Customer("4", "Peter","Parker","Male","peter@email.com");
    }

    @AfterEach
    void tearDown() {
        customerOne = null;
        customerTwo = null;
        customerThree = null;
        customerFour = null;
    }

    @Test
    public void givenExistingCustomerEmailThenShouldReturnCustomerDetails() throws Exception {
        when(customerService.getCustomerByEmail(any()))
                .thenReturn(customerOne);
        MvcResult mvcResult = mockMvc.perform(
                get("/customer?email=nick@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.encodeAsString("")))
                .andExpect(status().isCreated())
                .andReturn();
        Customer customer = toObjectFromJson(mvcResult, Customer.class);
        assertThat(customer.getEmail()).isEqualTo(customerOne.getEmail());
        verify(customerService, times(1))
                .getCustomerByEmail(any());
    }

    @Test
    public void whenCalledShouldReturnAllCustomerDetails() throws Exception {
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customerFour);
        customerList.add(customerOne);
        customerList.add(customerTwo);
        customerList.add(customerThree);
        when(customerService.getAllCustomers())
                .thenReturn(customerList);
        MvcResult mvcResult = mockMvc.perform(
                get("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.encodeAsString("")))
                .andExpect(status().isCreated())
                .andReturn();
    }


    private <T> T toObjectFromJson(MvcResult result, Class<T> toClass) throws Exception {
        return this.mapper.readValue(result.getResponse().getContentAsString(), toClass);
    }
}