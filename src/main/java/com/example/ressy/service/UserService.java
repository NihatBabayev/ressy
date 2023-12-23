package com.example.ressy.service;

import com.example.ressy.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public interface UserService {
    void saveUser(UserDTO userDTO, String type);

    boolean isUserExists(String email);

    void updatePassword(String userEmail, String newPassword);

    ResponseModel<UserProfileDTO> getUserDetailsForProfile(String userEmail) throws JsonProcessingException;

    ResponseModel<String> uploadProfilePhoto(String userEmail, MultipartFile file) throws IOException;

    String getUserProfilePhoto(String userEmail);

    byte[] getUserProfilePhotoAsBytes(String userEmail);

    ResponseModel<String> uploadProfilePhotoWithBase64(String userEmail, PhotoDTO photoDTO);

    ResponseModel<String> getUserProfilePhotoBase64(String userEmail);

    ResponseModel<byte[]> getUserProfilePhotoAsBytesFromBase64(String userEmail) throws UnsupportedEncodingException;

    ResponseModel<String> addUserDetails(String userEmail, DetailsDTO detailsDTO);

    ResponseModel<String> login(AuthRequest authRequest);

    ResponseModel<String> editUserDetails(String userEmail, UserProfileDTO userProfileDTO);
}
