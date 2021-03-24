package com.stackroute.kafkaconsumer.exception;


/**
 * Custom exception to handle user already exists condition
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
