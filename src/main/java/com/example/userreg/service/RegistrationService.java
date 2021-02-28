package com.example.userreg.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import com.example.userreg.model.ConfirmationToken;
import com.example.userreg.model.RegistrationRequest;
import com.example.userreg.model.User;
import com.example.userreg.model.UserRole;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidatorService emailValidatorService;
    private ConfirmationTokenService confirmationTokenService;
    private UserService userService;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidatorService.test(request.getEmail());  
        if (!isValidEmail) {
            throw new IllegalStateException("email is not valid");
        }
        
        return userService.signUpUser(
            new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                UserRole.USER
            )
        );
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableAppUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }
    
}
