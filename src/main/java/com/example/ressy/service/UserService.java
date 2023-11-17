package com.example.lenny.service;

import com.example.lenny.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void saveUser(UserDTO userDTO, String type);

    boolean isUserExists(String email);
}
