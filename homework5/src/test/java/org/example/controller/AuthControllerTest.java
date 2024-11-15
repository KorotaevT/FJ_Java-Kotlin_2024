package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.AbstractTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.example.MockObjects.passwordResetRequest;
import static org.example.MockObjects.userLoginRequest;
import static org.example.MockObjects.userRegistrationRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class AuthControllerTest extends AbstractTestContainer {

    public static final String API_AUTH_REGISTER = "/api/v1/auth/register";
    public static final String API_AUTH_LOGIN = "/api/v1/auth/login";
    public static final String API_AUTH_LOGOUT = "/api/v1/auth/logout";
    public static final String API_AUTH_RESET_PASSWORD = "/api/v1/auth/reset-password";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Test
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testRegisterUser() throws Exception {
        mockMvc.perform(post(API_AUTH_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Registration successful"));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testLoginUser() throws Exception {
        mockMvc.perform(post(API_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testLogoutUser() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        mockMvc.perform(post(API_AUTH_LOGOUT)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout successful!"));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testResetPassword() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        mockMvc.perform(post(API_AUTH_RESET_PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordResetRequest))
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successful!"));
    }

}