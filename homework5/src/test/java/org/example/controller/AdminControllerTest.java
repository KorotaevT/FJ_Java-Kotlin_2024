//package org.example.controller;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import lombok.RequiredArgsConstructor;
//import org.example.AbstractTestContainer;
//import org.springframework.test.annotation.DirtiesContext;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//
//@RequiredArgsConstructor
//public class AdminControllerTest extends AbstractTestContainer {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    @DirtiesContext
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void testGetAdminInfo() throws Exception {
//        mockMvc.perform(get("/api/v1/admin"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Admin check"));
//    }
//
//    @Test
//    @DirtiesContext
//    @WithMockUser(username = "user", roles = {"USER"})
//    public void testGetAdminInfoForbidden() throws Exception {
//        mockMvc.perform(get("/api/v1/admin"))
//                .andExpect(status().isForbidden())
//                .andExpect(content().string("Forbidden"));
//    }
//
//}
