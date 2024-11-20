package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.PasswordResetRequest;
import org.example.dto.request.RegistrationRequest;
import org.example.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "API for user authentication and account management.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Invalid registration details")
    })
    public String register(@Valid @RequestBody RegistrationRequest registrationDTO) {
        authService.register(registrationDTO);
        return "Registration successful";
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates the user and returns a JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid credentials")
    })
    public String login(@RequestBody LoginRequest loginDTO, HttpServletResponse response) {
        return authService.authenticate(loginDTO);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logs out the user by clearing the JWT token.")
    @ApiResponse(responseCode = "200", description = "Logout successful")
    public String logout(HttpServletResponse response) {
        authService.logout(response);
        return "Logout successful!";
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset user password", description = "Resets the user's password after verifying the reset code.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successful"),
            @ApiResponse(responseCode = "400", description = "Invalid reset details or verification code")
    })
    public String resetPassword(@RequestBody PasswordResetRequest passwordResetDTO) {
        authService.resetPassword(passwordResetDTO);
        return "Password reset successful!";
    }

}