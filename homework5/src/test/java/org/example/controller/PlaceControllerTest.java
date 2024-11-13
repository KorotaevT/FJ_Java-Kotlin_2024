//package org.example.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.AbstractTestContainer;
//import org.example.dto.request.PlaceDetailsRequest;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RequiredArgsConstructor
//public class PlaceControllerTest extends AbstractTestContainer {
//
//    private static final String BASE_URL = "/api/v1/places";
//
//    @Test
//    public void shouldCreateAndReturnPlace() throws Exception {
//        var placeDetailsRequest = new PlaceDetailsRequest("Test Place");
//        var requestJson = objectMapper.writeValueAsString(placeDetailsRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Test Place"));
//
//        mockMvc.perform(get(BASE_URL + "/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Test Place"));
//    }
//
//    @Test
//    public void shouldUpdatePlace() throws Exception {
//        var createRequest = new PlaceDetailsRequest("Test Place");
//        var createRequestJson = objectMapper.writeValueAsString(createRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createRequestJson))
//                .andExpect(status().isOk());
//
//        var updateRequest = new PlaceDetailsRequest("Updated Place");
//        var updateRequestJson = objectMapper.writeValueAsString(updateRequest);
//
//        mockMvc.perform(put(BASE_URL + "/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(updateRequestJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Place"));
//    }
//
//    @Test
//    public void shouldGetAllPlaces() throws Exception {
//        mockMvc.perform(get(BASE_URL))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray());
//    }
//
//    @Test
//    public void shouldGetPlaceById() throws Exception {
//        var placeDetailsRequest = new PlaceDetailsRequest("Test Place");
//        var requestJson = objectMapper.writeValueAsString(placeDetailsRequest);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get(BASE_URL + "/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Test Place"));
//    }
//
//    @Test
//    public void shouldDeletePlace() throws Exception {
//        var placeDetailsRequest = new PlaceDetailsRequest("Test Place");
//        var requestJson = objectMapper.writeValueAsString(placeDetailsRequest);
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