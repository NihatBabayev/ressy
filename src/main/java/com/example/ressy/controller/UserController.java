package com.example.ressy.controller;

import com.example.ressy.dto.ResponseModel;
import com.example.ressy.dto.UserProfileDTO;
import com.example.ressy.security.JwtService;
import com.example.ressy.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final JwtService jwtService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ResponseModel<UserProfileDTO>> getUserDetails(HttpServletRequest request) throws JsonProcessingException {
        String userEmail = jwtService.extractUsernameFromHeader(request.getHeader("Authorization"));
        return new ResponseEntity<>(userService.getUserDetailsForProfile( userEmail), HttpStatus.OK);
    }
    @PostMapping("/photo")
    public ResponseEntity<ResponseModel<String>> uploadProfilePhoto(@RequestParam("file") MultipartFile file,
                                                     HttpServletRequest request) throws IOException {
        String userEmail = jwtService.extractUsernameFromHeader(request.getHeader("Authorization"));

        return new ResponseEntity<>(userService.uploadProfilePhoto(userEmail, file), HttpStatus.OK);
    }
    @GetMapping("/photo")
    public ResponseEntity<ResponseModel<String>> getProfilePhoto(HttpServletRequest request){
        String userEmail = jwtService.extractUsernameFromHeader(request.getHeader("Authorization"));
        String photoBase64Encoded = userService.getUserProfilePhoto(userEmail);
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setData(photoBase64Encoded);
        responseModel.setMessage("Base 64 encoded photo is returned successfully.");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
}
