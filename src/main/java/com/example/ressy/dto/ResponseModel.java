package com.example.ressy.dto;

import lombok.Data;

@Data
public class ResponseModel<T> {
    T data;
    String message;
}