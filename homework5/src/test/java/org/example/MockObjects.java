package org.example;

import org.example.dto.request.EventDetailsRequest;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.PasswordResetRequest;
import org.example.dto.request.RegistrationRequest;

import java.time.LocalDate;

public class MockObjects {

    public static final String userUsername = "user";
    public static final String adminUsername = "admin";

    public static final String password = "password";

    public static RegistrationRequest userRegistrationRequest = new RegistrationRequest(userUsername + 1, password);
    public static LoginRequest userLoginRequest = new LoginRequest(userUsername, password, true);

    public static RegistrationRequest adminRegistrationRequest = new RegistrationRequest(adminUsername + 1, password);
    public static LoginRequest adminLoginRequest = new LoginRequest(adminUsername, password, true);

    public static PasswordResetRequest passwordResetRequest = new PasswordResetRequest("user", "password", "0000");

    public static EventDetailsRequest eventDetailsRequest = new EventDetailsRequest("event", LocalDate.now(), 1L);

    public static EventDetailsRequest updatedEventDetailsRequest = new EventDetailsRequest("updatedEvent", LocalDate.now(), 1L);
}