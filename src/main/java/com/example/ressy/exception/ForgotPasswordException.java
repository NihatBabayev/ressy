package com.example.ressy.exception;

public class ForgotPasswordException extends RuntimeException{
    public static final String DEFAULT_MESSAGE = "Unsuccessful Forgot Password ";
    public ForgotPasswordException(){
        super(DEFAULT_MESSAGE);
    }
}
