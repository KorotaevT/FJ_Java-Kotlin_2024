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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private CustomRepository<Location> repository;

    @InjectMocks
    private LocationService locationService;

    @Test
    void testGetAllLocations() {
        Location moscowLocation = createLocation("msk", "Москва");
        Location kazanLocation = createLocation("ekb", "Екатеринбург");
        when(repository.findAll()).thenReturn(Arrays.asList(moscowLocation, kazanLocation));

        Collection<LocationRequest> result = locationService.getAllLocations();

        Iterator<LocationRequest> iterator = result.iterator();

        assertThat(iterator.next().getName()).isEqualTo("Москва");
        assertThat(iterator.next().getSlug()).isEqualTo("ekb");
    }

    @Test
    void testGetLocationById() {
        Location moscowLocation = createLocation("msk", "Москва");
        when(repository.findById(1L)).thenReturn(moscowLocation);

        LocationRequest result = locationService.getLocationById(1L);

        assertThat(result.getName()).isEqualTo("Москва");
    }

    @Test
    void testCreateLocation() {
        ObservableImpl<Location> observable = new ObservableImpl<>();
        Observer<Location> observerMock = mock(Observer.class);
        observable.addObserver(observerMock);

        LocationRequest request = new LocationRequest("msk", "Москва");
        Location location = new Location(request.getSlug(), request.getName());

        observable.notifyObservers(location);

        verify(observerMock, times(1)).update(any(Location.class));
    }


    @Test
    void testCreateLocationDuringInitialize() {
        LocationResponse request = new LocationResponse();
        request.setSlug("msk");
        request.setName("Москва");

        locationService.createLocation(request);

        ArgumentCaptor<Location> locationCaptor = ArgumentCaptor.forClass(Location.class);
        verify(repository).save(locationCaptor.capture());

        List<Location> capturedLocations = locationCaptor.getAllValues();
        assertThat(capturedLocations).hasSize(1);

        Location capturedLocation = capturedLocations.getFirst();
        assertThat(capturedLocation.getSlug()).isEqualTo("msk");
        assertThat(capturedLocation.getName()).isEqualTo("Москва");
    }

    @Test
    void testUpdateLocationById() {
        LocationRequest request = createLocationRequest("msk", "Москва");

        locationService.updateLocation(1L, request);

        ArgumentCaptor<Location> locationCaptor = ArgumentCaptor.forClass(Location.class);
        verify(repository).update(eq(1L), locationCaptor.capture());

        List<Location> capturedLocations = locationCaptor.getAllValues();
        assertThat(capturedLocations).hasSize(1);

        Location capturedLocation = capturedLocations.getFirst();
        assertThat(capturedLocation.getName()).isEqualTo("Москва");
        assertThat(capturedLocation.getSlug()).isEqualTo("msk");
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
        LocationRequest request = createLocationRequest("msk", "Москва");
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

    private Location createLocation(String slug, String name) {
        return new Location(slug, name);
    }

    private LocationRequest createLocationRequest(String slug, String name) {
        return new LocationRequest(slug, name);
    }

}