package com.example.ressy.exception;

public class NullPhotoBase64Exception extends RuntimeException{
    public static final String DEFAULT_MESSAGE = "Photo doesn't exist";
    public NullPhotoBase64Exception(){
        super(DEFAULT_MESSAGE);
    }
}
