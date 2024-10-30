package org.example.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.LocationResponse;
import org.example.pattern.command.Command;
import org.example.service.KudagoService;
import org.example.service.LocationService;

@Slf4j
@RequiredArgsConstructor
public class FetchAndSaveLocationsCommand implements Command {

    private final KudagoService kudagoService;
    private final LocationService locationService;

    @Override
    public void execute() {
        var locations = kudagoService.getLocations();

        if (locations == null) {
            log.info("No locations fetched. Received null response.");
        } else {
            log.info("Fetched {} locations.", locations.length);

            for (LocationResponse location : locations) {
                locationService.createLocation(location);
            }
        }
    }

}