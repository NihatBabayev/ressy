
package com.example.ressy.service.Impl;

import com.example.ressy.dto.UserDTO;
import com.example.ressy.entity.Customer;
import com.example.ressy.entity.Professional;
import com.example.ressy.entity.User;
import com.example.ressy.exception.UserAlreadyExistsException;
import com.example.ressy.exception.UserNotFoundException;
import com.example.ressy.repository.CustomerRepository;
import com.example.ressy.repository.ProfessionalRepository;
import com.example.ressy.repository.UserRepository;
import com.example.ressy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ProfessionalRepository professionalRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void saveUser(UserDTO userDTO, String type) {

        if (isUserExists(userDTO.getEmail())) {
            throw new UserAlreadyExistsException();
        } else {
            String customerString = "customer";
            String professionalString = "professional";
            User user = new User();
            user.setFirstname(userDTO.getFirstname());
            user.setLastname(userDTO.getLastname());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            if (type.equals(customerString)) {

                user.setRole("ROLE_CUSTOMER");
                userRepository.save(user);

                Customer customer = new Customer();
                customer.setUser(user);
                customerRepository.save(customer);

            } else if (type.equals(professionalString)) {
                user.setRole("ROLE_PROFESSIONAL");
                userRepository.save(user);

                Professional professional = new Professional();
                professional.setUser(user);
                professional.setProfessionName(userDTO.getProfession());
                professionalRepository.save(professional);
            }
//            emailService.sendEmail(userDTO.getEmail(), "Successful Registration", "Hey, " + userDTO.getFirstname() +" " +userDTO.getLastname()+" welcome to  lenny.");
   //TODO send email to succesfully registrated users
        }
    }

    @Override
    public boolean isUserExists(String email) {
        if (userRepository.findByEmail(email) != null) {
            return true;
        }
        else
            return false;
    }

    @Override
    public void updatePassword(String userEmail, String newPassword) {
        if(!isUserExists(userEmail)){
            throw new UserNotFoundException();
        }
        userRepository.updatePasswordByUserEmail(userEmail, newPassword);
    }
}