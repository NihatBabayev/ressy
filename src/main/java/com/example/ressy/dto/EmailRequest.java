package com.example.ressy.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class EmailRequest implements Serializable {
    String title;
    String text;
    String recipient;
}
