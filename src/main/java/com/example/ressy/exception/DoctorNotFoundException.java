package com.example.ressy.exception;

public class DoctorNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Doctor Not Found";

    public DoctorNotFoundException(){
        super(DEFAULT_MESSAGE);
    }

}
