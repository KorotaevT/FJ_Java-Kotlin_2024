package org.example.util;

import org.example.dto.response.CategoryResponse;
import org.example.dto.response.LocationResponse;
import org.example.service.CategoryService;
import org.example.service.KudagoService;
import org.example.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @BeforeEach
    void setUp() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        dataInitializer = new DataInitializer(categoryService, locationService, kudagoService, fixedThreadPool, scheduledThreadPool);
    }

    @Test
    void testInit_PositiveScenario() throws InterruptedException {
        CategoryResponse[] categories = {new CategoryResponse(), new CategoryResponse()};
        LocationResponse[] locations = {new LocationResponse(), new LocationResponse()};

        when(kudagoService.getCategories()).thenReturn(categories);
        when(kudagoService.getLocations()).thenReturn(locations);

        CountDownLatch latch = new CountDownLatch(1);
        dataInitializer.init();
        latch.await(1, TimeUnit.SECONDS);

        ArgumentCaptor<CategoryResponse> categoryCaptor = ArgumentCaptor.forClass(CategoryResponse.class);
        ArgumentCaptor<LocationResponse> locationCaptor = ArgumentCaptor.forClass(LocationResponse.class);

        verify(categoryService, times(categories.length)).createCategory(categoryCaptor.capture());
        verify(locationService, times(locations.length)).createLocation(locationCaptor.capture());

        List<CategoryResponse> capturedCategories = categoryCaptor.getAllValues();
        List<LocationResponse> capturedLocations = locationCaptor.getAllValues();

        for (CategoryResponse category : categories) {
            assertTrue(capturedCategories.contains(category));
        }

        for (LocationResponse location : locations) {
            assertTrue(capturedLocations.contains(location));
        }
    }

    @Test
    void testInit_NoCategoriesFetched() throws InterruptedException {
        when(kudagoService.getCategories()).thenReturn(null);
        LocationResponse[] locations = {new LocationResponse(), new LocationResponse()};
        when(kudagoService.getLocations()).thenReturn(locations);

        CountDownLatch latch = new CountDownLatch(1);
        dataInitializer.init();
        latch.await(1, TimeUnit.SECONDS);

        ArgumentCaptor<LocationResponse> locationCaptor = ArgumentCaptor.forClass(LocationResponse.class);

        verify(categoryService, never()).createCategory(any(CategoryResponse.class));
        verify(locationService, times(locations.length)).createLocation(locationCaptor.capture());

        List<LocationResponse> capturedLocations = locationCaptor.getAllValues();

        for (LocationResponse location : locations) {
            assertTrue(capturedLocations.contains(location));
        }
    }

    @Test
    void testInit_NoLocationsFetched() throws InterruptedException {
        CategoryResponse[] categories = {new CategoryResponse(), new CategoryResponse()};
        when(kudagoService.getCategories()).thenReturn(categories);
        when(kudagoService.getLocations()).thenReturn(null);

        CountDownLatch latch = new CountDownLatch(1);
        dataInitializer.init();
        latch.await(1, TimeUnit.SECONDS);

        ArgumentCaptor<CategoryResponse> categoryCaptor = ArgumentCaptor.forClass(CategoryResponse.class);

        verify(categoryService, times(categories.length)).createCategory(categoryCaptor.capture());
        verify(locationService, never()).createLocation(any(LocationResponse.class));

        List<CategoryResponse> capturedCategories = categoryCaptor.getAllValues();

        for (CategoryResponse category : categories) {
            assertTrue(capturedCategories.contains(category));
        }
    }

    @Test
    void testInit_NoCategoriesAndLocationsFetched() throws InterruptedException {
        when(kudagoService.getCategories()).thenReturn(null);
        when(kudagoService.getLocations()).thenReturn(null);

        CountDownLatch latch = new CountDownLatch(1);
        dataInitializer.init();
        latch.await(1, TimeUnit.SECONDS);

        verify(categoryService, never()).createCategory(any(CategoryResponse.class));
        verify(locationService, never()).createLocation(any(LocationResponse.class));
    }

}