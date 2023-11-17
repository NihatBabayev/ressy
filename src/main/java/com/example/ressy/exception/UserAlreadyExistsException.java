package com.example.lenny.exception;

public class UserAlreadyExistsException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "User already exists with this email";
    public UserAlreadyExistsException(){
        super(DEFAULT_MESSAGE);
    }
}
