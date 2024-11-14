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

//    @Test
//    @Sql({
//            "classpath:db/insert-data.sql",
//    })
//    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void testRegisterUser() throws Exception {
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userRegistrationRequest)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Registration successful"));
//    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testLoginUser() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
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
        var token = "Bearer " + authService.authenticate(userLoginRequest);
        mockMvc.perform(post("/api/v1/auth/logout")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout successful!"));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testResetPassword() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);
        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordResetRequest))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successful!"));
    }

}