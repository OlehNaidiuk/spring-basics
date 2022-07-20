package com.naidiuk.webJdbcJsonWithSpring.errors;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
