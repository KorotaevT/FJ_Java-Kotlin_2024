package org.example.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.CategoryResponse;
import org.example.dto.response.LocationResponse;
import org.example.service.CategoryService;
import org.example.service.KudagoService;
import org.example.service.LocationService;
import org.example.timed.Timed;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final CategoryService categoryService;
    private final LocationService locationService;
    private final KudagoService kudagoService;

    @EventListener(ApplicationReadyEvent.class)
    @Timed
    public void init() {
        log.info("Starting data initialization...");

        var categories = kudagoService.getCategories();
        if (categories == null) {
            log.info("No categories fetched. Received null response.");
        } else {
            log.info("Fetched {} categories.", categories.length);

            for (CategoryResponse category : categories) {
                categoryService.createCategory(category);
            }
        }

        var locations = kudagoService.getLocations();
        if (locations == null) {
            log.info("No locations fetched. Received null response.");
        } else {
            log.info("Fetched {} locations.", locations.length);

            for (LocationResponse location : locations) {
                locationService.createLocation(location);
            }
        }

        log.info("Data initialization completed.");
    }

}