package com.example.userreg.service;

import java.util.Optional;

import com.example.userreg.model.ConfirmationToken;
import com.example.userreg.repository.ConfirmationTokenRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

	public void setConfirmedAt(String token) {
	}
    
}
