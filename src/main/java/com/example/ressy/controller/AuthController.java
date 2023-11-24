package com.example.ressy.controller;

import com.example.ressy.dto.AuthRequest;
import com.example.ressy.dto.ResponseModel;
import com.example.ressy.dto.UserDTO;
import com.example.ressy.exception.ForgotPasswordException;
import com.example.ressy.exception.InvalidOtpCodeException;
import com.example.ressy.exception.UserAlreadyExistsException;
import com.example.ressy.exception.UserNotFoundException;
import com.example.ressy.security.CustomUserDetailsService;
import com.example.ressy.security.JwtService;
import com.example.ressy.service.EmailService;
import com.example.ressy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseModel<String>> signupUser(@RequestBody UserDTO userDTO,
                                                            @RequestParam("type") String type,
                                                            @RequestParam(value = "otp", required = false) String otpCode) {

        String email = userDTO.getEmail();
        if (userService.isUserExists(email)) {
            throw new UserAlreadyExistsException();
        }
        if (otpCode == null) {
            String randomOtp = String.valueOf(new Random().nextInt(9000) + 1000);
            redisTemplate.opsForValue().set(email, randomOtp, 1, TimeUnit.MINUTES);
            ResponseModel<String> responseModel = new ResponseModel<>();
            //TODO SEND Email to user(otp code) notification ms
            emailService.sendEmail(email, "OTP code", "Your OTP code is \n" + randomOtp + "\nDon't share it with others.");
            responseModel.setMessage("OTP sent to your email. Check your inbox.");
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } else {
            String storedOtp = (String) redisTemplate.opsForValue().get(email);
            if (storedOtp != null && storedOtp.equals(otpCode)) {
                userService.saveUser(userDTO, type);
                String jwtToken = jwtService.generateToken(email);
                redisTemplate.delete(email);
                ResponseModel<String> responseModel = new ResponseModel<>();
                responseModel.setData(jwtToken);
                return new ResponseEntity<>(responseModel, HttpStatus.CREATED);
            } else {
                throw new InvalidOtpCodeException();
            }
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseModel<String>> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            ResponseModel<String> responseModel = new ResponseModel<>();
            String jwtToken = jwtService.generateToken(authRequest.getUsername());
            responseModel.setData(jwtToken);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } else {
            throw new UserNotFoundException();
        }
    }

    @PostMapping("/forgot")
    public ResponseEntity<ResponseModel<String>> forgotPassword(@RequestParam(value = "email") String email) {
        if (!userService.isUserExists(email)) {
            throw new UserNotFoundException();
        }
        String tempToken = jwtService.generateToken(email);
        String resetLink = "http://ec2-18-202-56-138.eu-west-1.compute.amazonaws.com:8080/index.html?token=" + tempToken;
        emailService.sendEmail(email, "Forgot Password", resetLink);
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setMessage("Reset link sent successfully");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @PostMapping("/forgot/web")
    public ResponseEntity<ResponseModel<String>> forgotPasswordWeb(@RequestBody AuthRequest authRequest, @RequestParam("token") String token) {
        String newPassword = authRequest.getPassword();
        String userEmail = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        if (jwtService.validateToken(token, userDetails)) {
            userService.updatePassword(userEmail, newPassword);
            ResponseModel<String> responseModel = new ResponseModel<>();
            responseModel.setMessage("Password updated successfully");
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } else {
            throw new ForgotPasswordException();
        }
    }

}
