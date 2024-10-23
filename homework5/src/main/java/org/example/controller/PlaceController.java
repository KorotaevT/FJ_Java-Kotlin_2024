package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.response.PlaceResponse;
import org.example.dto.request.PlaceDetailsRequest;
import org.example.service.PlaceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public List<PlaceResponse> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/{id}")
    public PlaceResponse getPlaceById(@PathVariable Long id) {
        return placeService.getPlaceById(id);
    }

    @PostMapping
    public PlaceResponse createPlace(@Valid @RequestBody PlaceDetailsRequest placeDetails) {
        return placeService.createPlace(placeDetails);
    }

    @PutMapping("/{id}")
    public PlaceResponse updatePlace(@PathVariable Long id, @Valid @RequestBody PlaceDetailsRequest placeDetails) {
        return placeService.updatePlace(id, placeDetails);
    }

    @DeleteMapping("/{id}")
    public void deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
    }

}