//package org.example.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.AbstractTestContainer;
//import org.example.dto.response.EventEntityResponse;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.jdbc.Sql;
//
//import static org.example.MockObjects.eventDetailsRequest;
//import static org.example.MockObjects.updatedEventDetailsRequest;
//import static org.example.MockObjects.userLoginRequest;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RequiredArgsConstructor
//public class EventControllerTest extends AbstractTestContainer {
//
//    private static final String BASE_URL = "/api/v1/events";
//
//    @Test
//    @Sql({
//            "classpath:db/clear-db.sql",
//            "classpath:db/insert-data.sql",
//    })
//    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void shouldCreateAndReturnEvent() throws Exception {
//        var token = "Bearer " + authService.authenticate(userLoginRequest);
//
//        var requestJson = objectMapper.writeValueAsString(eventDetailsRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson)
//                        .header("Authorization", token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("event"))
//                .andExpect(jsonPath("$.placeId").value(1));
//
//        var response = mockMvc.perform(get(BASE_URL + "/1")
//                .header("Authorization", token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("event"))
//                .andReturn();
//
//        var responseBody = response.getResponse().getContentAsString();
//        var createdEvent = objectMapper.readValue(responseBody, EventEntityResponse.class);
//
//        assertThat(createdEvent).isNotNull();
//        assertThat(createdEvent.getId()).isNotNull();
//        assertThat(createdEvent.getName()).isEqualTo("event");
//    }
//
//    @Test
//    @Sql({
//            "classpath:db/insert-data.sql",
//    })
//    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void shouldUpdateEvent() throws Exception {
//        var token = "Bearer " + authService.authenticate(userLoginRequest);
//        var createRequestJson = objectMapper.writeValueAsString(eventDetailsRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createRequestJson)
//                        .header("Authorization", token))
//                .andExpect(status().isOk());
//
//        var updateRequestJson = objectMapper.writeValueAsString(updatedEventDetailsRequest);
//
//        mockMvc.perform(put(BASE_URL + "/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(updateRequestJson)
//                        .header("Authorization", token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("updatedEvent"));
//    }
//
//    @Test
//    @Sql({
//            "classpath:db/insert-data.sql",
//    })
//    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void shouldGetAllEvents() throws Exception {
//        var token = "Bearer " + authService.authenticate(userLoginRequest);
//        mockMvc.perform(get(BASE_URL)
//                .header("Authorization", token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray());
//    }
//
//    @Test
//    @Sql({
//            "classpath:db/insert-data.sql",
//    })
//    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void shouldGetEventById() throws Exception {
//        var token = "Bearer " + authService.authenticate(userLoginRequest);
//        var requestJson = objectMapper.writeValueAsString(eventDetailsRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson)
//                        .header("Authorization", token))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get(BASE_URL + "/1")
//                .header("Authorization", token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("event"));
//    }
//
//    @Test
//    @Sql({
//            "classpath:db/insert-data.sql",
//    })
//    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void shouldFilterEvents() throws Exception {
//        var token = "Bearer " + authService.authenticate(userLoginRequest);
//        var requestJson = objectMapper.writeValueAsString(eventDetailsRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson)
//                        .header("Authorization", token))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get(BASE_URL + "/filter")
//                        .param("name", "Test Event")
//                        .header("Authorization", token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("event"));
//    }
//
//    @Test
//    @Sql({
//            "classpath:db/insert-data.sql",
//    })
//    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void shouldDeleteEvent() throws Exception {
//        var token = "Bearer " + authService.authenticate(userLoginRequest);
//        var requestJson = objectMapper.writeValueAsString(eventDetailsRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestJson)
//                .header("Authorization", token))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(delete(BASE_URL + "/1")
//                .header("Authorization", token))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get(BASE_URL + "/1")
//                .header("Authorization", token))
//                .andExpect(status().isNotFound());
//    }
//
//}