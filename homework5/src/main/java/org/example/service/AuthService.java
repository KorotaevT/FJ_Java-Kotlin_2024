package org.example.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new UserAlreadyExistsException("User with that name has already been registered!");
        }

        var user = new UserEntity();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public void logout(HttpServletResponse response) {
        response.setHeader("Authorization", "");
    }

    public String authenticate(LoginRequest loginRequest) {
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
        return token;
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        if (!userRepository.existsByUsername(passwordResetRequest.getUsername())) {
            throw new UsernameNotFoundException("User with that name has not been registered!");
        }

        if (verificationCode.equals(passwordResetRequest.getVerificationCode())) {
            var user = userRepository.findByUsername(passwordResetRequest.getUsername());
            user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new VerificationCodeException("Verification code is incorrect");
        }
    }

}