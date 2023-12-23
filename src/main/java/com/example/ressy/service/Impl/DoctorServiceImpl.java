package com.example.ressy.service.Impl;

import com.example.ressy.dto.DoctorDTO;
import com.example.ressy.dto.HomeRequestMessage;
import com.example.ressy.dto.UserResponseMessage;
import com.example.ressy.entity.Doctor;
import com.example.ressy.entity.User;
import com.example.ressy.exception.DoctorNotFoundException;
import com.example.ressy.repository.DoctorRepository;
import com.example.ressy.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final RabbitTemplate rabbitTemplate;
    private final DoctorRepository doctorRepository;

    @RabbitListener(queues = "user-request-queue")
    public void processHomeRequest(HomeRequestMessage homeRequestMessage) {
        List<DoctorDTO> doctorDTOList = getDoctors(homeRequestMessage.getProfession());
        UserResponseMessage responseMessage = new UserResponseMessage(doctorDTOList, homeRequestMessage.getProfession());
        rabbitTemplate.convertAndSend("user-response-queue", responseMessage, message -> {
            message.getMessageProperties().setContentType("application/json");
            return message;
        });
    }

    public List<DoctorDTO> getDoctors(String profession) {
        List<Doctor> doctors;
        if ("all".equals(profession)) {
            doctors = doctorRepository.findAll(); //made change in this line
        } else {
            doctors = doctorRepository.findAllByProfessionName(profession);
        }
        if(doctors == null){
            throw new DoctorNotFoundException();
        }
        List<DoctorDTO> doctorDTOs = doctors.stream()
                .map(DoctorDTO::fromEntity)
                .collect(Collectors.toList());
        return doctorDTOs;
    }
    public String getDoctorEmailFromDetails(DoctorDTO doctorDTO){
        List<Doctor> doctors = doctorRepository.findAllByProfessionName(doctorDTO.getProfession());
        String doctorEmail = "default";
        for (Doctor doctor:doctors) {
            if(doctorDTO.getFirstname().equals(doctor.getUser().getFirstname()) && doctorDTO.getLastname().equals(doctor.getUser().getLastname())){
                doctorEmail = doctor.getUser().getEmail();
                break;
            }
        }
        if("default".equals(doctorEmail)){
            throw new DoctorNotFoundException();
        }
        return doctorEmail;
    }

//    public List<DoctorDTO> getAllDoctors() {
//        List<Doctor> doctors = doctorRepository.findAll();
//
//        List<DoctorDTO> doctorDTOs = doctors.stream()
//                .map(DoctorDTO::fromEntity)
//                .collect(Collectors.toList());
//
//        System.out.println("DATA IS SENDING TO HOME SERVICE !!!");
//        System.out.println("DATA IS:");
//        for (DoctorDTO doctorDTO : doctorDTOs) {
//            System.out.println(doctorDTO);
//        }
//        System.out.println();
//        return doctorDTOs;
//    }

//    public void sendDoctorDTOsToHomeService() {
//        List<DoctorDTO> doctorDTOs = getAllDoctors();
//        System.out.println("ConvertandSend TRIGGERED!!!");
//        rabbitTemplate.convertAndSend("doctor.exchange", "doctor.routingKey", doctorDTOs);
//    }
}