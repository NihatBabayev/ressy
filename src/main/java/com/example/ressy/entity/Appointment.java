package com.example.ressy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "appointments", schema = "ressy")
public class Appointment {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;


    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "professional_id")
    private Professional professional;


}
