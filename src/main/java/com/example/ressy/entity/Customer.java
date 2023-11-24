package com.example.ressy.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customers",schema = "ressy")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}