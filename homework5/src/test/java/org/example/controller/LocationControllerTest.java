package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.AbstractTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.example.dto.request.LocationRequest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

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

    private static final String BASE_URL = "/api/v1/locations";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private static final long LOCATION_ID = 1;
    private static final long NON_EXISTENT_LOCATION_ID = 999;

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllLocations() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        List<LocationRequest> locations = getLocations();

        when(locationService.getAllLocations()).thenReturn(locations);

        mockMvc.perform(get(BASE_URL)
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
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        LocationRequest location = createLocationRequest("msk", "Москва");

        when(locationService.getLocationById(anyLong())).thenReturn(location);

        mockMvc.perform(get(BASE_URL + "/{id}", LOCATION_ID)
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
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        when(locationService.getLocationById(anyLong())).thenReturn(null);

        mockMvc.perform(get(BASE_URL + "/{id}", NON_EXISTENT_LOCATION_ID)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createLocation() throws Exception {
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        mockMvc.perform(post(BASE_URL)
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
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        mockMvc.perform(post(BASE_URL)
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
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);

        doNothing().when(locationService).updateLocation(anyLong(), any(LocationRequest.class));

        mockMvc.perform(put(BASE_URL + "/{id}", LOCATION_ID)
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
        var token = BEARER_PREFIX + authService.authenticate(userLoginRequest);
        doNothing().when(locationService).deleteLocation(anyLong());

        mockMvc.perform(delete(BASE_URL + "/{id}", LOCATION_ID)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk());
    }

}