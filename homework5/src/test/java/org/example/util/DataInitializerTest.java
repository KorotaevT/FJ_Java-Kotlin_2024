package org.example.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.dto.response.CategoryResponse;
import org.example.dto.response.LocationResponse;
import org.example.service.CategoryService;
import org.example.service.KudagoService;
import org.example.service.LocationService;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private LocationService locationService;

    @Mock
    private KudagoService kudagoService;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void testInit_PositiveScenario() {
        CategoryResponse[] categories = {new CategoryResponse(), new CategoryResponse()};
        LocationResponse[] locations = {new LocationResponse(), new LocationResponse()};

        when(kudagoService.getCategories()).thenReturn(categories);
        when(kudagoService.getLocations()).thenReturn(locations);

        dataInitializer.init();

        ArgumentCaptor<CategoryResponse> categoryCaptor = ArgumentCaptor.forClass(CategoryResponse.class);
        ArgumentCaptor<LocationResponse> locationCaptor = ArgumentCaptor.forClass(LocationResponse.class);

        verify(categoryService, times(categories.length)).createCategory(categoryCaptor.capture());
        verify(locationService, times(locations.length)).createLocation(locationCaptor.capture());

        List<CategoryResponse> capturedCategories = categoryCaptor.getAllValues();
        List<LocationResponse> capturedLocations = locationCaptor.getAllValues();

        assertThat(capturedCategories).containsExactlyInAnyOrder(categories);
        assertThat(capturedLocations).containsExactlyInAnyOrder(locations);
    }

    @Test
    void testInit_NoCategoriesFetched() {
        when(kudagoService.getCategories()).thenReturn(null);
        LocationResponse[] locations = {new LocationResponse(), new LocationResponse()};
        when(kudagoService.getLocations()).thenReturn(locations);

        dataInitializer.init();

        ArgumentCaptor<LocationResponse> locationCaptor = ArgumentCaptor.forClass(LocationResponse.class);

        verify(categoryService, never()).createCategory(any(CategoryResponse.class));
        verify(locationService, times(locations.length)).createLocation(any(LocationResponse.class));
        verify(locationService, times(locations.length)).createLocation(locationCaptor.capture());

        List<LocationResponse> capturedLocations = locationCaptor.getAllValues();

        assertThat(capturedLocations).containsExactlyInAnyOrder(locations);
    }

    @Test
    void testInit_NoLocationsFetched() {
        CategoryResponse[] categories = {new CategoryResponse(), new CategoryResponse()};
        when(kudagoService.getCategories()).thenReturn(categories);
        when(kudagoService.getLocations()).thenReturn(null);

        dataInitializer.init();

        ArgumentCaptor<CategoryResponse> categoryCaptor = ArgumentCaptor.forClass(CategoryResponse.class);

        verify(categoryService, times(categories.length)).createCategory(categoryCaptor.capture());
        verify(locationService, never()).createLocation(any(LocationResponse.class));

        List<CategoryResponse> capturedCategories = categoryCaptor.getAllValues();

        assertThat(capturedCategories).containsExactlyInAnyOrder(categories);
    }

    @Test
    void testInit_NoCategoriesAndLocationsFetched() {
        when(kudagoService.getCategories()).thenReturn(null);
        when(kudagoService.getLocations()).thenReturn(null);

        dataInitializer.init();

        verify(categoryService, never()).createCategory(any(CategoryResponse.class));
        verify(locationService, never()).createLocation(any(LocationResponse.class));

        verifyNoMoreInteractions(categoryService);
        verifyNoMoreInteractions(locationService);
    }

}