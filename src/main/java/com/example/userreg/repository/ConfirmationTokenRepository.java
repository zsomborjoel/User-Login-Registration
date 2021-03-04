package com.example.userreg.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.userreg.model.ConfirmationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
 
    Optional<ConfirmationToken> findByToken(String token);

	int updateConfirmedAt(String token, LocalDateTime now);

}
