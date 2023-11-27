package com.example.ressy.service;

import com.example.ressy.dto.ResponseModel;
import com.example.ressy.dto.UserDTO;
import com.example.ressy.dto.UserProfileDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserService {
    void saveUser(UserDTO userDTO, String type);

    boolean isUserExists(String email);

    void updatePassword(String userEmail, String newPassword);

    ResponseModel<UserProfileDTO> getUserDetailsForProfile(String userEmail) throws JsonProcessingException;

    ResponseModel<String> uploadProfilePhoto(String userEmail, MultipartFile file) throws IOException;

    String getUserProfilePhoto(String userEmail);
}
