package com.example.ressy.service;

import com.example.ressy.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void saveUser(UserDTO userDTO, String type);

    boolean isUserExists(String email);

    void updatePassword(String userEmail, String newPassword);
}
