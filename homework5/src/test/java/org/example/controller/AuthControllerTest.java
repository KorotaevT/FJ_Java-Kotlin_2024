//package org.example.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.example.AbstractTestContainer;
//import org.example.dto.request.LoginRequest;
//import org.example.dto.request.PasswordResetRequest;
//import org.example.dto.request.RegistrationRequest;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RequiredArgsConstructor
//public class AuthControllerTest extends AbstractTestContainer {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @DirtiesContext
//    public void testRegisterUser() throws Exception {
//        var registrationRequest = new RegistrationRequest("testuser", "password123");
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(registrationRequest)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Registration successful"));
//    }
//
//    @Test
//    @DirtiesContext
//    public void testLoginUser() throws Exception {
//        var loginRequest = new LoginRequest("testuser", "password123", true);
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Login successful"));
//    }
//
//    @Test
//    @DirtiesContext
//    public void testLogoutUser() throws Exception {
//        mockMvc.perform(post("/api/v1/auth/logout"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Logout successful!"));
//    }
//
//    @Test
//    @DirtiesContext
//    public void testResetPassword() throws Exception {
//        var passwordResetRequest = new PasswordResetRequest("testuser", "newPassword123", "123456");
//
//        mockMvc.perform(post("/api/v1/auth/reset-password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(passwordResetRequest)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Password reset successful!"));
//    }
//
//}