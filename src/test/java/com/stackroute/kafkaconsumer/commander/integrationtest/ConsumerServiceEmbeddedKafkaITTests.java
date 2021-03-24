package com.stackroute.kafkaconsumer.commander.integrationtest;

import com.stackroute.kafkaconsumer.domain.Customer;
import com.stackroute.kafkaconsumer.service.Consumer;
import com.stackroute.kafkaconsumer.service.CustomerService;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@EmbeddedKafka(partitions = 1, controlledShutdown = false, topics = {"customer_details"},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@SpringBootTest
@DirtiesContext
public class ConsumerServiceEmbeddedKafkaITTests {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ConsumerServiceEmbeddedKafkaITTests.class);

    private static String TOPIC = "consumer_details";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private Consumer consumer;

    private KafkaTemplate<String, Customer> kafkaTemplate;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;


    @BeforeEach
    public void setUp() {
        Map<String, Object> producerProperties =
                KafkaTestUtils.producerProps(embeddedKafkaBroker);
        ProducerFactory<String, Customer> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProperties, new StringSerializer(), new JsonSerializer<Customer>());

        kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(TOPIC);
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
                .getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer,
                    embeddedKafkaBroker.getPartitionsPerTopic());
        }

    }


    @Test
    public void givenCustomerWhenSentToKafkaTopicThenReceivedSuccessfully() throws InterruptedException {

        Customer customer = new Customer("1","Nick", "Jonas", "Male", "nick@email.com");

        kafkaTemplate.send(TOPIC, customer);
        consumer.getLatch().await(5000, TimeUnit.MILLISECONDS);
        Assertions.assertThat(consumer.getLatch().getCount()).isEqualTo(1);
    }
}
