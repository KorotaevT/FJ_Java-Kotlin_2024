package org.example;

import org.example.dto.request.CategoryRequest;
import org.example.dto.request.EventDetailsRequest;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.PasswordResetRequest;
import org.example.dto.request.PlaceDetailsRequest;
import org.example.dto.request.RegistrationRequest;
import org.example.dto.request.LocationRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class MockObjects {

    public static final String userUsername = "user";
    public static final String adminUsername = "admin";

    public static final String password = "password";

    public static RegistrationRequest userRegistrationRequest = new RegistrationRequest(userUsername + 1, password);
    public static LoginRequest userLoginRequest = new LoginRequest(userUsername, password, true);

    public static RegistrationRequest adminRegistrationRequest = new RegistrationRequest(adminUsername + 1, password);
    public static LoginRequest adminLoginRequest = new LoginRequest(adminUsername, password, true);

    public static PasswordResetRequest passwordResetRequest = new PasswordResetRequest("user", "password", "0000");

    public static EventDetailsRequest eventDetailsRequest = new EventDetailsRequest("event", LocalDate.now(), 1L);

    public static EventDetailsRequest updatedEventDetailsRequest = new EventDetailsRequest("updatedEvent", LocalDate.now(), 1L);

    public static PlaceDetailsRequest placeDetailsRequest = new PlaceDetailsRequest("testPlace");

    public static PlaceDetailsRequest updatedPlaceDetailsRequest = new PlaceDetailsRequest("updatedTestPlace");

    public static final String LOCATION_JSON = """
            [
                {
                    "slug": "msk",
                    "name": "Москва"
                },
                {
                    "slug": "ekb",
                    "name": "Екатеринбург"
                }
            ]
            """;

    public static final String LOCATION_JSON_SINGLE = """
            {
                "slug": "msk",
                "name": "Москва"
            }
            """;

    public static final String INVALID_LOCATION_JSON = """
            {
                "slug": "",
                "name": ""
            }
            """;

    public static final String CATEGORY_JSON = """
            [
                {
                    "slug": "airports",
                    "name": "Аэропорты"
                },
                {
                    "slug": "amusement",
                    "name": "Развлечения"
                }
            ]
            """;

    public static final String CATEGORY_JSON_SINGLE = """
            {
                "slug": "airports",
                "name": "Аэропорты"
            }
            """;

    public static final String INVALID_CATEGORY_JSON = """
            {
                "slug": "",
                "name": ""
            }
            """;

    public static LocationRequest createLocationRequest(String slug, String name) {
        return new LocationRequest(slug, name);
    }

    public static List<LocationRequest> getLocations() {
        return Arrays.asList(
                createLocationRequest("msk", "Москва"),
                createLocationRequest("ekb", "Екатеринбург")
        );
    }

    public static CategoryRequest createCategoryRequest(String slug, String name) {
        return new CategoryRequest(slug, name);
    }

    public static List<CategoryRequest> getCategories() {
        return Arrays.asList(
                createCategoryRequest("airports", "Аэропорты"),
                createCategoryRequest("amusement", "Развлечения")
        );
    }

}