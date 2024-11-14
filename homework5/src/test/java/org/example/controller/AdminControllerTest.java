package org.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.example.MockObjects.adminLoginRequest;
import static org.example.MockObjects.userLoginRequest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import lombok.RequiredArgsConstructor;
import org.example.AbstractTestContainer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RequiredArgsConstructor
public class AdminControllerTest extends AbstractTestContainer {

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAdminInfo() throws Exception {
        var token = "Bearer " + authService.authenticate(adminLoginRequest);
        mockMvc.perform(get("/api/v1/admin")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin check"));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAdminInfoForbidden() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);
        mockMvc.perform(get("/api/v1/admin")
                .header("Authorization", token))
                .andExpect(status().isForbidden());
    }

}
