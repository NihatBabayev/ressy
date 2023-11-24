package com.example.ressy.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "professionals", schema = "ressy")
@Data
public class Professional {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "profession_name")
    private String professionName;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
