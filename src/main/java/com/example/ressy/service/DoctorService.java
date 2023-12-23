package com.example.ressy.service;

import com.example.ressy.dto.DoctorDTO;
import com.example.ressy.dto.HomeRequestMessage;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface DoctorService {
    void processHomeRequest(HomeRequestMessage homeRequestMessage);

    List<DoctorDTO> getDoctors(String profession);
    String getDoctorEmailFromDetails(DoctorDTO doctorDTO);
}
