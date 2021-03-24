## Problem Statement: Kafka Producer-Consumer Microservices
        
## Problem Statement:
  - kafka-consumer: This service receives the data from the kafka of a particular topic.
    *  this service receives data using kafka listener which is configured using kafkaConsumerConfig class
    *  the kafka listener logs the data received and saves it in mongodb database
    *  the topic, address and groupId for configuration are provided in "application.yml"

## Running the application locally after cloning

    > After implementing the requirements, execute the following maven command in the parent/root project

            mvn clean package
    
    > Start Kafka. Instruction for installation and starting these are provided in below section

    > The Service KafkaProducerApplication have to be started
    
    > Run the following command `bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic customer_details` and send a 'Customer' object data

## Following software needs to be available/installed in the local environment
**MongoDb**

    > Use the following commands on ubuntu terminal for installing MongoDb
            
        sudo apt update
        sudo apt install -y mongodb
            
    > Check the Server status using following command
                    
        sudo systemctl status mongodb
        
    > Use the following command to connect to MongoDb server using mongo cli        
            mongo          
**Kafka**

     > Refer the steps provided at the below url to install and start apache kafka

         https://kafka.apache.org/quickstart

## Instructions
  - Take care of whitespace/trailing whitespace
  - Do not change the provided class/method names unless instructed
  - Ensure your code compiles without any errors/warning/deprecations
  - Follow best practices while coding
