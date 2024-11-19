package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.AbstractTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.example.MockObjects.API_PLACES;
import static org.example.MockObjects.AUTHORIZATION_HEADER;
import static org.example.MockObjects.BEARER_PREFIX;
import static org.example.MockObjects.ID_URL_PATH;
import static org.example.MockObjects.NAME_JSON_PATH;
import static org.example.MockObjects.PLACE_NAME;
import static org.example.MockObjects.UPDATED_PLACE_NAME;
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

    private String getAuthToken() {
        return BEARER_PREFIX + authService.authenticate(userLoginRequest);
    }

    private void createPlace(String token) throws Exception {
        var requestJson = objectMapper.writeValueAsString(placeDetailsRequest);
        mockMvc.perform(post(API_PLACES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldCreateAndReturnPlace() throws Exception {
        var token = getAuthToken();

        createPlace(token);

        mockMvc.perform(get(API_PLACES + "/3")
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath(NAME_JSON_PATH).value(PLACE_NAME));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldUpdatePlace() throws Exception {
        var token = getAuthToken();

        createPlace(token);

        var updateRequestJson = objectMapper.writeValueAsString(updatedPlaceDetailsRequest);
        mockMvc.perform(put(API_PLACES + ID_URL_PATH)
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
        var token = getAuthToken();
        mockMvc.perform(get(API_PLACES)
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
        var token = getAuthToken();

        mockMvc.perform(get(API_PLACES + ID_URL_PATH)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath(NAME_JSON_PATH).value("place1"));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldDeletePlace() throws Exception {
        var token = getAuthToken();

        createPlace(token);

        mockMvc.perform(delete(API_PLACES + ID_URL_PATH)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());

        mockMvc.perform(get(API_PLACES + ID_URL_PATH)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isNotFound());
    }

}