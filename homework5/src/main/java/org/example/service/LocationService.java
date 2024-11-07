package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.LocationRequest;
import org.example.dto.response.LocationResponse;
import org.example.model.Location;
import org.example.pattern.observer.impl.ObservableImpl;
import org.example.repository.CustomRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LocationService extends ObservableImpl<Location> {

    private final CustomRepository<Location> repository;

    public LocationService(CustomRepository<Location> repository) {
        this.repository = repository;
        addObserver(repository);
    }

    public Collection<LocationRequest> getAllLocations() {
        log.info("Fetching all locations.");
        Collection<LocationRequest> locations = repository
                .findAll()
                .stream()
                .map(this::parseModelToRequest)
                .collect(Collectors.toList());
        log.info("Fetched {} locations.", locations.size());
        return locations;
    }

    public LocationRequest getLocationById(Long id) {
        log.info("Fetching location by id: {}", id);
        LocationRequest location = parseModelToRequest(repository.findById(id));
        log.info("Fetched location: {}", location);
        return location;
    }

    public void createLocation(LocationRequest request) {
        log.info("Creating location: {}", request);
        notifyObservers(parseRequestToModel(request));
    }

    public void createLocation(LocationResponse response) {
        log.info("Creating location from response: {}", response);
        repository.save(parseResponseToModel(response));
        log.info("Location '{}' created successfully from response.", response.getName());
    }

    public void updateLocation(Long id, LocationRequest request) {
        log.info("Updating location with id: {} with data: {}", id, request);
        repository.update(id, parseRequestToModel(request));
        log.info("Location with id {} updated successfully.", id);
    }

    public void deleteLocation(Long id) {
        log.info("Deleting location with id: {}", id);
        repository.delete(id);
        log.info("Location with id {} deleted successfully.", id);
    }

    private LocationRequest parseModelToRequest(Location location) {
        if (location == null) {
            return null;
        }
        return new LocationRequest(location.getSlug(), location.getName());
    }

    private Location parseRequestToModel(LocationRequest request) {
        return new Location(request.getSlug(), request.getName());
    }

    private Location parseResponseToModel(LocationResponse response) {
        return new Location(response.getSlug(), response.getName());
    }

    public void restore() {
        repository.restore();
    }

}