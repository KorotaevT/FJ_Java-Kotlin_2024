package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordResetRequest {

    @NotNull
    private String username;

    @NotNull
    private String newPassword;

    @NotNull
    private String verificationCode;
}