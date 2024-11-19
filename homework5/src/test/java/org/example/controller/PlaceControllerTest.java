package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.AbstractTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.example.MockObjects.placeDetailsRequest;
import static org.example.MockObjects.updatedPlaceDetailsRequest;
import static org.example.MockObjects.userLoginRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class PlaceControllerTest extends AbstractTestContainer {

    private static final String BASE_URL = "/api/v1/places";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String NAME_JSON_PATH = "$.name";
    private static final String ID_URL_PATH = "/1";
    private static final String PLACE_NAME = "testPlace";
    private static final String UPDATED_PLACE_NAME = "updatedTestPlace";

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldCreateAndReturnPlace() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        var requestJson = objectMapper.writeValueAsString(placeDetailsRequest);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath(NAME_JSON_PATH).value(PLACE_NAME));

        mockMvc.perform(get(BASE_URL + "/3")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath(NAME_JSON_PATH).value(PLACE_NAME));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldUpdatePlace() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        var createRequestJson = objectMapper.writeValueAsString(placeDetailsRequest);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());

        var updateRequestJson = objectMapper.writeValueAsString(updatedPlaceDetailsRequest);

        mockMvc.perform(put(BASE_URL + ID_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath(NAME_JSON_PATH).value(UPDATED_PLACE_NAME));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetAllPlaces() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        mockMvc.perform(get(BASE_URL)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetPlaceById() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        mockMvc.perform(get(BASE_URL + ID_URL_PATH)
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath(NAME_JSON_PATH).value("place1"));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldDeletePlace() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        var requestJson = objectMapper.writeValueAsString(placeDetailsRequest);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());

        mockMvc.perform(delete(BASE_URL + ID_URL_PATH)
                .header("Authorization", token))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + ID_URL_PATH)
                .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

}