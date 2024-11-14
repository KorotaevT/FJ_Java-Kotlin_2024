package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.AbstractTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.example.dto.request.LocationRequest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

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

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllLocations() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);
        List<LocationRequest> locations = Arrays.asList(
                createLocationRequest("msk", "Москва"),
                createLocationRequest("ekb", "Екатеринбург")
        );

        when(locationService.getAllLocations()).thenReturn(locations);

        mockMvc.perform(get("/api/v1/locations")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        [
                            {
                                "slug": "msk",
                                "name": "Москва"
                            },
                            {
                                "slug": "ekb",
                                "name": "Екатеринбург"
                            }
                        ]
                        """
                ));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getLocationById() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);
        LocationRequest location = createLocationRequest("msk", "Москва");

        when(locationService.getLocationById(anyLong())).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/{id}", 1)
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                            "slug": "msk",
                            "name": "Москва"
                        }
                        """
                ));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getLocationByNonExistentId() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);
        when(locationService.getLocationById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/v1/locations/{id}", 999)
                .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createLocation() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);
        String locationRequest = """
                {
                    "slug": "msk",
                    "name": "Москва"
                }
                """;

        mockMvc.perform(post("/api/v1/locations")
                        .content(locationRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk());

        verify(locationService, times(1)).createLocation(any(LocationRequest.class));
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createLocation_InvalidRequest() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);
        String invalidLocationRequest = """
                {
                    "slug": "",
                    "name": ""
                }
                """;

        mockMvc.perform(post("/api/v1/locations")
                        .content(invalidLocationRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateLocationById() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);
        String locationRequest = """
                {
                    "slug": "msk",
                    "name": "Москва"
                }
                """;

        doNothing().when(locationService).updateLocation(anyLong(), any(LocationRequest.class));

        mockMvc.perform(put("/api/v1/locations/{id}", 1)
                        .content(locationRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({
            "classpath:db/insert-data.sql",
    })
    @Sql(value = "classpath:db/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteLocationById() throws Exception {
        var token = "Bearer " + authService.authenticate(userLoginRequest);
        doNothing().when(locationService).deleteLocation(anyLong());

        mockMvc.perform(delete("/api/v1/locations/{id}", 1)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    private LocationRequest createLocationRequest(String slug, String name) {
        return new LocationRequest(slug, name);
    }

}