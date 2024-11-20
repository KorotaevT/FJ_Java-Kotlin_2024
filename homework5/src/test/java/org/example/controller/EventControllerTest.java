package org.example.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.AbstractTestContainer;
import org.example.dto.response.EventEntityResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.example.MockObjects.API_EVENTS;
import static org.example.MockObjects.AUTHORIZATION_HEADER;
import static org.example.MockObjects.BEARER_PREFIX;
import static org.example.MockObjects.EVENT_CREATED_RESPONSE_NAME;
import static org.example.MockObjects.UPDATED_EVENT_NAME;
import static org.example.MockObjects.eventDetailsRequest;
import static org.example.MockObjects.updatedEventDetailsRequest;
import static org.example.MockObjects.userLoginRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class EventControllerTest extends AbstractTestContainer {

    @Test
    @Sql({
            "classpath:db/clear-db.sql",
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldCreateAndReturnEvent() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        var requestJson = objectMapper.writeValueAsString(eventDetailsRequest);

        mockMvc.perform(post(API_EVENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(EVENT_CREATED_RESPONSE_NAME))
                .andExpect(jsonPath("$.placeId").value(1));

        var response = mockMvc.perform(get(API_EVENTS + "/1")
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(EVENT_CREATED_RESPONSE_NAME))
                .andReturn();

        var responseBody = response.getResponse().getContentAsString();
        var createdEvent = objectMapper.readValue(responseBody, EventEntityResponse.class);

        assertThat(createdEvent).isNotNull();
        assertThat(createdEvent.getId()).isNotNull();
        assertThat(createdEvent.getName()).isEqualTo(EVENT_CREATED_RESPONSE_NAME);
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldUpdateEvent() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        var createRequestJson = objectMapper.writeValueAsString(eventDetailsRequest);

        mockMvc.perform(post(API_EVENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());

        var updateRequestJson = objectMapper.writeValueAsString(updatedEventDetailsRequest);

        mockMvc.perform(put(API_EVENTS + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(UPDATED_EVENT_NAME));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetAllEvents() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        mockMvc.perform(get(API_EVENTS)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetEventById() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        var requestJson = objectMapper.writeValueAsString(eventDetailsRequest);

        mockMvc.perform(post(API_EVENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());

        mockMvc.perform(get(API_EVENTS + "/1")
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(EVENT_CREATED_RESPONSE_NAME));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldFilterEvents() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        var requestJson = objectMapper.writeValueAsString(eventDetailsRequest);

        mockMvc.perform(post(API_EVENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());

        mockMvc.perform(get(API_EVENTS + "/filter")
                        .param("name", eventDetailsRequest.getName())
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(EVENT_CREATED_RESPONSE_NAME));
    }

    @Test
    @Transactional
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldDeleteEvent() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        var requestJson = objectMapper.writeValueAsString(eventDetailsRequest);

        mockMvc.perform(post(API_EVENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());

        mockMvc.perform(delete(API_EVENTS + "/1")
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());

        mockMvc.perform(get(API_EVENTS + "/1")
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isNotFound());
    }

}