package com.example.ressy.exception;

public class UserNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "User not found";
    public UserNotFoundException(){
        super(DEFAULT_MESSAGE);
    }
}