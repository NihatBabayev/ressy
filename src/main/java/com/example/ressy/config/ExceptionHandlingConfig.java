package com.example.ressy.config;

import com.example.ressy.dto.ResponseModel;
import com.example.ressy.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class ExceptionHandlingConfig {

    @ExceptionHandler
            ({
                    UserAlreadyExistsException.class,
                    UsernameNotFoundException.class,
                    InvalidOtpCodeException.class,
                    ForgotPasswordException.class,
                    IllegalFileTypeException.class,
                    NullPhotoBase64Exception.class,
                    DoctorNotFoundException.class
            })
    public ResponseEntity<ResponseModel<String>> handleCustomExceptions(Exception ex) throws Exception {
        ResponseModel<String> exceptionResponseModel = new ResponseModel<>();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        StringBuilder errorMessage = new StringBuilder(ex.getMessage());



        if (ex instanceof UserAlreadyExistsException  || ex instanceof UsernameNotFoundException || ex instanceof InvalidOtpCodeException || ex instanceof ForgotPasswordException || ex instanceof IllegalFileTypeException || ex instanceof NullPhotoBase64Exception || ex instanceof DoctorNotFoundException) {
            httpStatus = HttpStatus.BAD_REQUEST;

        }


        exceptionResponseModel.setMessage(String.valueOf(errorMessage));
        return new ResponseEntity<>(exceptionResponseModel, httpStatus);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseModel<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ResponseModel<String> responseModel = new ResponseModel<>();
        ex.getConstraintViolations().forEach(v -> {
            String message = v.getMessage();
            errorMessage.append(message).append(". ");
        });
        responseModel.setMessage(errorMessage.toString());

        return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
    }
}