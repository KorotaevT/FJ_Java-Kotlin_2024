//package org.example.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.AbstractTestContainer;
//import org.example.dto.request.EventDetailsRequest;
//import org.example.dto.response.EventEntityResponse;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//
//import java.time.LocalDate;
//
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
//    public void shouldCreateAndReturnEvent() throws Exception {
//        var eventDetailsRequest = new EventDetailsRequest("Test Event", LocalDate.now(), 1L);
//        var requestJson = objectMapper.writeValueAsString(eventDetailsRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Test Event"))
//                .andExpect(jsonPath("$.placeId").value(1));
//
//        var response = mockMvc.perform(get(BASE_URL + "/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Test Event"))
//                .andReturn();
//
//        var responseBody = response.getResponse().getContentAsString();
//        var createdEvent = objectMapper.readValue(responseBody, EventEntityResponse.class);
//
//        assertThat(createdEvent).isNotNull();
//        assertThat(createdEvent.getId()).isNotNull();
//        assertThat(createdEvent.getName()).isEqualTo("Test Event");
//    }
//
//    @Test
//    public void shouldUpdateEvent() throws Exception {
//        var createRequest = new EventDetailsRequest("Test Event", LocalDate.now(), 1L);
//        var createRequestJson = objectMapper.writeValueAsString(createRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createRequestJson))
//                .andExpect(status().isOk());
//
//        var updateRequest = new EventDetailsRequest("Updated Event", LocalDate.now(), 1L);
//        var updateRequestJson = objectMapper.writeValueAsString(updateRequest);
//
//        mockMvc.perform(put(BASE_URL + "/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(updateRequestJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Event"));
//    }
//
//    @Test
//    public void shouldGetAllEvents() throws Exception {
//        mockMvc.perform(get(BASE_URL))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray());
//    }
//
//    @Test
//    public void shouldGetEventById() throws Exception {
//        var eventDetailsRequest = new EventDetailsRequest("Test Event", LocalDate.now(), 1L);
//        var requestJson = objectMapper.writeValueAsString(eventDetailsRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get(BASE_URL + "/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Test Event"));
//    }
//
//    @Test
//    public void shouldFilterEvents() throws Exception {
//        var eventDetailsRequest = new EventDetailsRequest("Test Event", LocalDate.now(), 1L);
//        var requestJson = objectMapper.writeValueAsString(eventDetailsRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get(BASE_URL + "/filter")
//                        .param("name", "Test Event"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("Test Event"));
//    }
//
//    @Test
//    public void shouldDeleteEvent() throws Exception {
//        var eventDetailsRequest = new EventDetailsRequest("Test Event", LocalDate.now(), 1L);
//        var requestJson = objectMapper.writeValueAsString(eventDetailsRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(delete(BASE_URL + "/1"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get(BASE_URL + "/1"))
//                .andExpect(status().isNotFound());
//    }
//
//}