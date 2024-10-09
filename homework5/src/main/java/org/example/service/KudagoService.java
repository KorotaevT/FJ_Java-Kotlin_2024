package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.response.CategoryResponse;
import org.example.dto.response.LocationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KudagoService {

    private static final String CATEGORIES_URL = "/place-categories";
    private static final String LOCATIONS_URL = "/locations";

    private final RestTemplate kudagoRestTemplate;

    public CategoryResponse[] getCategories() {
        return kudagoRestTemplate.getForObject(CATEGORIES_URL, CategoryResponse[].class);
    }

    public LocationResponse[] getLocations() {
        return kudagoRestTemplate.getForObject(LOCATIONS_URL, LocationResponse[].class);
    }

}