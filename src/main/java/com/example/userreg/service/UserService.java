package com.example.userreg.service;

import com.example.userreg.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice.Local;

import static com.example.userreg.constants.UserConstants.USER_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.userreg.model.ConfirmationToken;
import com.example.userreg.model.User;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public String signUpUser(User user) {
        boolean userExists = userRepository
            .findByEmail(user.getEmail())
            .isPresent();

        if (userExists) {
            throw new IllegalStateException("email already taken");
        }    

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            now,
            now.plusMinutes(15),
            user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

	public void enableAppUser(String email) {
	}

}
