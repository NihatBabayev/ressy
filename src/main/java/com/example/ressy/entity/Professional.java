package com.example.ressy.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "services", schema = "ressy")
@Data
public class Professional {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "service_name")
    private String serviceName;

}
