package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.LocationRequest;
import org.example.service.LocationService;
import org.example.timed.Timed;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Timed
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public Collection<LocationRequest> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    public LocationRequest getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @PostMapping
    public void createLocation(@RequestBody LocationRequest request) {
        locationService.createLocation(request);
    }

    @PutMapping("/{id}")
    public void updateLocation(@PathVariable Long id, @RequestBody LocationRequest request) {
        locationService.updateLocation(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
    }

}