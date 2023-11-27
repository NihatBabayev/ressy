package com.example.ressy.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UserProfileDTO {
    String firstname;
    String lastname;
    LocalDate joinDate;
}
