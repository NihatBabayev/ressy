package com.example.lenny.dto;

import lombok.Data;

@Data
public class ResponseModel<T> {
    T data;
    String message;
}