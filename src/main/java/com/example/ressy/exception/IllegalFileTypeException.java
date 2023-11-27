package com.example.ressy.exception;

public class IllegalFileTypeException extends RuntimeException{
    public static final String DEFAULT_MESSAGE = "File type is not eligible";
    public IllegalFileTypeException(){
        super(DEFAULT_MESSAGE);
    }
}
