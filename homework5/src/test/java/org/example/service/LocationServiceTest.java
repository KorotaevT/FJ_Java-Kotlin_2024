package org.example.service;

import org.example.dto.request.LocationRequest;
import org.example.dto.response.LocationResponse;
import org.example.model.Location;
import org.example.pattern.observer.Observer;
import org.example.pattern.observer.impl.ObservableImpl;
import org.example.repository.CustomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.MockObjects.EKATERINBURG_NAME;
import static org.example.MockObjects.EKATERINBURG_SLUG;
import static org.example.MockObjects.MOSCOW_NAME;
import static org.example.MockObjects.MOSCOW_SLUG;
import static org.example.MockObjects.createLocation;
import static org.example.MockObjects.createLocationRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private CustomRepository<Location> repository;

    @Mock
    private Observer<Location> observer;

    @InjectMocks
    private LocationService locationService;

    @Test
    void testGetAllLocations() {
        Location moscowLocation = createLocation(MOSCOW_SLUG, MOSCOW_NAME);
        Location kazanLocation = createLocation(EKATERINBURG_SLUG, EKATERINBURG_NAME);

        when(repository.findAll()).thenReturn(Arrays.asList(moscowLocation, kazanLocation));

        Collection<LocationRequest> result = locationService.getAllLocations();

        Iterator<LocationRequest> iterator = result.iterator();
        assertThat(iterator.next().getName()).isEqualTo(MOSCOW_NAME);
        assertThat(iterator.next().getSlug()).isEqualTo(EKATERINBURG_SLUG);
    }

    @Test
    void testGetLocationById() {
        Location moscowLocation = createLocation(MOSCOW_SLUG, MOSCOW_NAME);
        when(repository.findById(1L)).thenReturn(moscowLocation);

        LocationRequest result = locationService.getLocationById(1L);

        assertThat(result.getName()).isEqualTo(MOSCOW_NAME);
    }

    @Test
    void testCreateLocation() {
        ObservableImpl<Location> observable = new ObservableImpl<>();
        observable.addObserver(observer);

        LocationRequest request = new LocationRequest(MOSCOW_SLUG, MOSCOW_NAME);
        Location location = new Location(request.getSlug(), request.getName());

        observable.notifyObservers(location);

        verify(observer, times(1)).update(any(Location.class));
    }

    @Test
    void testCreateLocationDuringInitialize() {
        LocationResponse request = new LocationResponse();
        request.setSlug(MOSCOW_SLUG);
        request.setName(MOSCOW_NAME);

        locationService.createLocation(request);

        ArgumentCaptor<Location> locationCaptor = ArgumentCaptor.forClass(Location.class);
        verify(repository).save(locationCaptor.capture());

        List<Location> capturedLocations = locationCaptor.getAllValues();
        assertThat(capturedLocations).hasSize(1);

        Location capturedLocation = capturedLocations.getFirst();
        assertThat(capturedLocation.getSlug()).isEqualTo(MOSCOW_SLUG);
        assertThat(capturedLocation.getName()).isEqualTo(MOSCOW_NAME);
    }

    @Test
    void testUpdateLocationById() {
        LocationRequest request = createLocationRequest(MOSCOW_SLUG, MOSCOW_NAME);

        locationService.updateLocation(1L, request);

        ArgumentCaptor<Location> locationCaptor = ArgumentCaptor.forClass(Location.class);
        verify(repository).update(eq(1L), locationCaptor.capture());

        List<Location> capturedLocations = locationCaptor.getAllValues();
        assertThat(capturedLocations).hasSize(1);

        Location capturedLocation = capturedLocations.getFirst();
        assertThat(capturedLocation.getName()).isEqualTo(MOSCOW_NAME);
        assertThat(capturedLocation.getSlug()).isEqualTo(MOSCOW_SLUG);
    }

    @Test
    void testDeleteLocationById() {
        locationService.deleteLocation(1L);

        verify(repository).delete(1L);
    }

    @Test
    void testGetLocationByNonExistentId() {
        when(repository.findById(1L)).thenReturn(null);

        LocationRequest result = locationService.getLocationById(1L);

        assertThat(result).isNull();
    }

    @Test
    void testUpdateLocationByNonExistentId() {
        LocationRequest request = createLocationRequest(MOSCOW_SLUG, MOSCOW_NAME);
        doThrow(new IllegalArgumentException()).when(repository).update(eq(1L), any(Location.class));

        assertThatThrownBy(() -> locationService.updateLocation(1L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testDeleteLocationByNonExistentId() {
        doThrow(new IllegalArgumentException()).when(repository).delete(1L);

        assertThatThrownBy(() -> locationService.deleteLocation(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

}