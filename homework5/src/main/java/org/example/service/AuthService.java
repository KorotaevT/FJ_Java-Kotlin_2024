package org.example.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.PasswordResetRequest;
import org.example.dto.request.RegistrationRequest;
import org.example.exceptions.UserAlreadyExistsException;
import org.example.exceptions.VerificationCodeException;
import org.example.model.enums.Role;
import org.example.model.UserEntity;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${verification.code}")
    private String verificationCode;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public void register(RegistrationRequest registrationRequest) {
        log.info("Starting registration process");
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            log.warn("User already exists");
            throw new UserAlreadyExistsException("User with that name has already been registered!");
        }

        var user = new UserEntity();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        log.info("Registration successful");
    }

    public void logout(HttpServletResponse response) {
        log.info("Starting logout process");
        response.setHeader("Authorization", "");
        log.info("Logout successful");
    }

    public String authenticate(LoginRequest loginRequest) {
        log.info("Starting authentication process");
        if (!userRepository.existsByUsername(loginRequest.getUsername())) {
            throw new UsernameNotFoundException("User with that name has not been registered!");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        var user = userRepository.findByUsername(loginRequest.getUsername());

        var token = jwtService.generateToken(user, loginRequest.getRememberMe());
        log.info("Authentication successful");
        return token;
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        log.info("Starting password reset process");
        if (!userRepository.existsByUsername(passwordResetRequest.getUsername())) {
            log.warn("User not found");
            throw new UsernameNotFoundException("User with that name has not been registered!");
        }

        if (verificationCode.equals(passwordResetRequest.getVerificationCode())) {
            var user = userRepository.findByUsername(passwordResetRequest.getUsername());
            user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
            userRepository.save(user);
            log.info("Password reset successful");
        } else {
            log.warn("Verification code is incorrect");
            throw new VerificationCodeException("Verification code is incorrect");
        }
    }

}