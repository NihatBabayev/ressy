
package com.example.ressy.service.Impl;

import com.example.ressy.dto.*;
import com.example.ressy.entity.Customer;
import com.example.ressy.entity.Doctor;
import com.example.ressy.entity.User;
import com.example.ressy.exception.NullPhotoBase64Exception;
import com.example.ressy.exception.UserAlreadyExistsException;
import com.example.ressy.exception.UserNotFoundException;
import com.example.ressy.repository.CustomerRepository;
import com.example.ressy.repository.DoctorRepository;
import com.example.ressy.repository.UserRepository;
import com.example.ressy.security.JwtService;
import com.example.ressy.service.EmailService;
import com.example.ressy.service.S3Service;
import com.example.ressy.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final S3Service s3Service;
    private final JwtService jwtService;


    @Override
    public ResponseModel<String> login(AuthRequest authRequest) {
        String jwtToken = jwtService.generateToken(authRequest.getUsername());
        Customer customer = customerRepository.findCustomerByUser_Email(authRequest.getUsername());
        ResponseModel<String> responseModel = new ResponseModel<>();

        if (customer != null) {
            responseModel.setMessage("customer");
        } else {
            responseModel.setMessage("doctor");
        }
        responseModel.setData(jwtToken);

        return responseModel;
    }
    @Transactional
    @Override
    public ResponseModel<String> editUserDetails(String userEmail, UserProfileDTO userProfileDTO) {
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new UserAlreadyExistsException();
        }
        else {
            user.setFirstname(userProfileDTO.getFirstname());
            user.setLastname(userProfileDTO.getLastname());
        }

        userRepository.save(user);
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setMessage("Edited User Details Successfully");
        return responseModel;
    }

    @Override
    public void saveUser(UserDTO userDTO, String type) {

        if (isUserExists(userDTO.getEmail())) {
            throw new UserAlreadyExistsException();
        } else {
            String customerString = "customer";
            String professionalString = "doctor";
            User user = new User();
            user.setFirstname(userDTO.getFirstname());
            user.setLastname(userDTO.getLastname());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setCreated(LocalDate.now());

            if (type.equals(customerString)) {

                user.setRole("ROLE_CUSTOMER");
                userRepository.save(user);

                Customer customer = new Customer();
                customer.setUser(user);
                customerRepository.save(customer);

            } else if (type.equals(professionalString)) {
                user.setRole("ROLE_DOCTOR");
                userRepository.save(user);

                Doctor doctor = new Doctor();
                doctor.setUser(user);
                doctor.setProfessionName(userDTO.getProfession());
                doctorRepository.save(doctor);
            }

            emailService.publishMessage("Successful registration", "Welcome to Ressy dear " + userDTO.getFirstname() + " " + userDTO.getLastname() + " . Book your appoitments with Ressy ", userDTO.getEmail()); //with notification MS

        }
    }

    @Override
    public boolean isUserExists(String email) {
        if (userRepository.findByEmail(email) != null) {
            return true;
        } else
            return false;
    }

    @Override
    public void updatePassword(String userEmail, String newPassword) {
        if (!isUserExists(userEmail)) {
            throw new UserNotFoundException();
        }
        userRepository.updatePasswordByUserEmail(userEmail, passwordEncoder.encode(newPassword));
    }

    @Override
    public ResponseModel<UserProfileDTO> getUserDetailsForProfile(String userEmail) throws JsonProcessingException {
        User user = userRepository.findByEmail(userEmail);
        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        LocalDate joinDate = user.getCreated();
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setFirstname(firstname);
        userProfileDTO.setLastname(lastname);
        userProfileDTO.setJoinDate(joinDate);

        ResponseModel<UserProfileDTO> responseModel = new ResponseModel<>();
        responseModel.setData(userProfileDTO);
        responseModel.setMessage("Success");
        return responseModel;
    }

    @Override
    public ResponseModel<String> uploadProfilePhoto(String userEmail, MultipartFile file) throws IOException {
        String photoName = "profile-photos/" + UUID.randomUUID() + file.getOriginalFilename();
        User user = userRepository.findByEmail(userEmail);
        user.setPhotoName(photoName);

        userRepository.save(user);
        s3Service.uploadPhoto(photoName, file);
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setMessage("profile photo added successfully.");
        return responseModel;
    }


    @Override
    public String getUserProfilePhoto(String userEmail) {
        String photoName = userRepository.findByEmail(userEmail).getPhotoName();
        return s3Service.getObjectFromS3AsBase64(photoName);
    }

    @Override
    public byte[] getUserProfilePhotoAsBytes(String userEmail) {
        String photoName = userRepository.findByEmail(userEmail).getPhotoName();
        return s3Service.getObjectFromS3AsBytes(photoName);
    }

    @Override
    public ResponseModel<String> uploadProfilePhotoWithBase64(String userEmail, PhotoDTO photoDTO) {
        User user = userRepository.findByEmail(userEmail);
        user.setPhotoBase64(photoDTO.getPhoto());
        userRepository.save(user);
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setMessage("profile photo added successfully.");
        return responseModel;
    }

    @Override
    public ResponseModel<String> getUserProfilePhotoBase64(String userEmail) {
        String photoBase64Encoded = userRepository.findByEmail(userEmail).getPhotoBase64();
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setData(photoBase64Encoded);
        responseModel.setMessage("Base 64 encoded photo is returned successfully.");
        return responseModel;
    }

    @Override
    public ResponseModel<byte[]> getUserProfilePhotoAsBytesFromBase64(String userEmail) throws UnsupportedEncodingException {
        User user = userRepository.findByEmail(userEmail);
        String photoBase64 = user.getPhotoBase64();
        ResponseModel<byte[]> responseModel = new ResponseModel<>();

        if (photoBase64 == null) {
            throw new NullPhotoBase64Exception();
        } else {
            byte[] decodedPhoto = Base64.getDecoder().decode(photoBase64);
            responseModel.setData(decodedPhoto);
            responseModel.setMessage("Success");
        }

        return responseModel;
    }

    @Override
    public ResponseModel<String> addUserDetails(String userEmail, DetailsDTO detailsDTO) {
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new UserNotFoundException();
        }

        user.setDob(detailsDTO.getDateOfBirth());
        user.setGender(detailsDTO.getGender());
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setMessage("User Details added successfully");
        return responseModel;
    }


}