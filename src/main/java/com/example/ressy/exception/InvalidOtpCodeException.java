package com.example.lenny.exception;

public class InvalidOtpCodeException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Provided otp code is invalid, try again";
    public InvalidOtpCodeException(){
        super(DEFAULT_MESSAGE);
    }
}
