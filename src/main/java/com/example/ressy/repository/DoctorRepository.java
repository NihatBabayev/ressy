package com.example.ressy.repository;


import com.example.ressy.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllByProfessionName(String profession);
    @Query("select d From Doctor d")
    List<Doctor> retrieveAllDoctors();
}