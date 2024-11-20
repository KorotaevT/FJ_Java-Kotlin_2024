package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.AbstractTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.example.MockObjects.API_AUTH_LOGIN;
import static org.example.MockObjects.API_AUTH_LOGOUT;
import static org.example.MockObjects.API_AUTH_REGISTER;
import static org.example.MockObjects.API_AUTH_RESET_PASSWORD;
import static org.example.MockObjects.AUTHORIZATION_HEADER;
import static org.example.MockObjects.BEARER_PREFIX;
import static org.example.MockObjects.PASSWORD_RESET_SUCCESS_MESSAGE;
import static org.example.MockObjects.REGISTRATION_SUCCESS_MESSAGE;
import static org.example.MockObjects.passwordResetRequest;
import static org.example.MockObjects.userLoginRequest;
import static org.example.MockObjects.userRegistrationRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class AuthControllerTest extends AbstractTestContainer {

    @Test
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testRegisterUser() throws Exception {
        mockMvc.perform(post(API_AUTH_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(REGISTRATION_SUCCESS_MESSAGE));
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
                .andExpect(status().isOk());
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
                .andExpect(content().string(PASSWORD_RESET_SUCCESS_MESSAGE));
    }

}