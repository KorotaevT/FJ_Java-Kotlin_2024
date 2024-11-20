package org.example;

import org.example.dto.request.CategoryRequest;
import org.example.dto.request.EventDetailsRequest;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.PasswordResetRequest;
import org.example.dto.request.PlaceDetailsRequest;
import org.example.dto.request.RegistrationRequest;
import org.example.dto.request.LocationRequest;
import org.example.model.Category;
import org.example.model.Location;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class MockObjects {

    public static final String API_ADMIN = "/api/v1/admin";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ADMIN_CHECK_RESPONSE = "Admin check";

    public static final String API_AUTH_REGISTER = "/api/v1/auth/register";
    public static final String API_AUTH_LOGIN = "/api/v1/auth/login";
    public static final String API_AUTH_LOGOUT = "/api/v1/auth/logout";
    public static final String API_AUTH_RESET_PASSWORD = "/api/v1/auth/reset-password";
    public static final String REGISTRATION_SUCCESS_MESSAGE = "Registration successful";
    public static final String PASSWORD_RESET_SUCCESS_MESSAGE = "Password reset successful!";

    public static final String API_CATEGORIES = "/api/v1/places/categories";
    public static final String API_CATEGORY_BY_ID = "/api/v1/places/categories/{id}";


    public static final String API_EVENTS = "/api/v1/events";

    public static final String EVENT_CREATED_RESPONSE_NAME = "event";
    public static final String UPDATED_EVENT_NAME = "updatedEvent";

    public static final String API_LOCATIONS = "/api/v1/locations";
    public static final String API_PLACES = "/api/v1/places";

    public static final String NAME_JSON_PATH = "$.name";
    public static final String ID_URL_PATH = "/1";
    public static final String PLACE_NAME = "testPlace";
    public static final String UPDATED_PLACE_NAME = "updatedTestPlace";

    public static final String userUsername = "user";
    public static final String adminUsername = "admin";

    public static final String password = "password";

    public static final String AIRPORTS_SLUG = "airports";
    public static final String AIRPORTS_NAME = "Аэропорты";
    public static final String AMUSEMENT_SLUG = "amusement";
    public static final String AMUSEMENT_NAME = "Развлечения";
    public static final String ANIMAL_SHELTERS_SLUG = "animal-shelters";
    public static final String ANIMAL_SHELTERS_NAME = "Питомники";
    public static final String THEATRE_SLUG = "theatre";
    public static final String THEATRE_NAME = "Театры";
    public static final String WORKSHOPS_SLUG = "workshops";
    public static final String WORKSHOPS_NAME = "Мастерские";

    public static final String EKATERINBURG_SLUG = "ekb";
    public static final String EKATERINBURG_NAME = "Екатеринбург";
    public static final String KAZAN_SLUG = "kzn";
    public static final String KAZAN_NAME = "Казань";
    public static final String MOSCOW_SLUG = "msk";
    public static final String MOSCOW_NAME = "Москва";
    public static final String NIZHNY_NOVGOROD_SLUG = "nnv";
    public static final String NIZHNY_NOVGOROD_NAME = "Нижний Новгород";
    public static final String ST_PETERSBURG_SLUG = "spb";
    public static final String ST_PETERSBURG_NAME = "Санкт-Петербург";

    public static RegistrationRequest userRegistrationRequest = new RegistrationRequest(userUsername + 1, password);
    public static LoginRequest userLoginRequest = new LoginRequest(userUsername, password, true);

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

    public static List<Category> getCategoriesAsCategory() {
        return Arrays.asList(
                new Category("airports", "Аэропорты"),
                new Category("amusement", "Развлечения")
        );
    }

    public static Location createLocation(String slug, String name) {
        return new Location(slug, name);
    }

}