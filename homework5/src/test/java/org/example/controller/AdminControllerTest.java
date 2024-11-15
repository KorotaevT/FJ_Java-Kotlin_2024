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

    public static final String API_ADMIN = "/api/v1/admin";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAdminInfo() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(adminLoginRequest);
        mockMvc.perform(get(API_ADMIN)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin check"));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAdminInfoForbidden() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        mockMvc.perform(get(API_ADMIN)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isForbidden());
    }

}