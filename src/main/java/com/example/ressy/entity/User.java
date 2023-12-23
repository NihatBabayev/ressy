package com.example.ressy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "users", schema = "ressy")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;
    private String password;
    private String role;
    private LocalDate created;
    private LocalDate dob;
    private String photoName;
    private String gender;

    @Column(columnDefinition = "TEXT")
    private String photoBase64;

    @Column(name = "is_active")
    private boolean isActive;
}