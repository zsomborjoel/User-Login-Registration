package com.example.userreg.service;

import com.example.userreg.model.RegistrationRequest;
import com.example.userreg.model.UserRole;

import org.apache.catalina.core.ApplicationPushBuilder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private EmailValidatorService emailValidatorService;
    private UserService userService;

    public String request(RegistrationRequest request) {
        boolean isValidEmail = emailValidatorService.test(request.getEmail());  
        if (!isValidEmail) {
            throw new IllegalStateException("email is not valid");
        }
        
        return userService.signUpUser(
            new ApplicationPushBuilder(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                UserRole.USER
            )
        );
    }
    
}
