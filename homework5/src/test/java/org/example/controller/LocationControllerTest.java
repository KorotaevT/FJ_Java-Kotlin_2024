package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.AbstractTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.example.dto.request.LocationRequest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.example.MockObjects.API_LOCATIONS;
import static org.example.MockObjects.AUTHORIZATION_HEADER;
import static org.example.MockObjects.BEARER_PREFIX;
import static org.example.MockObjects.INVALID_LOCATION_JSON;
import static org.example.MockObjects.LOCATION_JSON;
import static org.example.MockObjects.LOCATION_JSON_SINGLE;
import static org.example.MockObjects.createLocationRequest;
import static org.example.MockObjects.getLocations;
import static org.example.MockObjects.userLoginRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class LocationControllerTest extends AbstractTestContainer {

    private String getAuthToken() {
        return BEARER_PREFIX + authService.authenticate(userLoginRequest);
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllLocations() throws Exception {
        var token = getAuthToken();
        List<LocationRequest> locations = getLocations();

        when(locationService.getAllLocations()).thenReturn(locations);

        mockMvc.perform(get(API_LOCATIONS)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(content().json(LOCATION_JSON));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getLocationById() throws Exception {
        var token = getAuthToken();
        LocationRequest location = createLocationRequest("msk", "Москва");

        when(locationService.getLocationById(anyLong())).thenReturn(location);

        mockMvc.perform(get(API_LOCATIONS + "/{id}", 1)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(content().json(LOCATION_JSON_SINGLE));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getLocationByNonExistentId() throws Exception {
        var token = getAuthToken();
        when(locationService.getLocationById(anyLong())).thenReturn(null);

        mockMvc.perform(get(API_LOCATIONS + "/{id}", 999)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createLocation() throws Exception {
        var token = getAuthToken();

        mockMvc.perform(post(API_LOCATIONS)
                        .content(LOCATION_JSON_SINGLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());

        verify(locationService, times(1)).createLocation(any(LocationRequest.class));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createLocation_InvalidRequest() throws Exception {
        var token = getAuthToken();

        mockMvc.perform(post(API_LOCATIONS)
                        .content(INVALID_LOCATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateLocationById() throws Exception {
        var token = getAuthToken();

        doNothing().when(locationService).updateLocation(anyLong(), any(LocationRequest.class));

        mockMvc.perform(put(API_LOCATIONS + "/{id}", 1)
                        .content(LOCATION_JSON_SINGLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteLocationById() throws Exception {
        var token = getAuthToken();
        doNothing().when(locationService).deleteLocation(anyLong());

        mockMvc.perform(delete(API_LOCATIONS + "/{id}", 1)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());
    }

}