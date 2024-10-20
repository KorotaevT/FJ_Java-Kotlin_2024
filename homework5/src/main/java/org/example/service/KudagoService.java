package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.CategoryResponse;
import org.example.dto.response.EventKudagoResponse;
import org.example.dto.response.LocationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Semaphore;

@Service
@RequiredArgsConstructor
@Slf4j
public class KudagoService {

    private static final String CATEGORIES_URL = "/place-categories";
    private static final String LOCATIONS_URL = "/locations";
    private static final String EVENTS_URL = "/events";

    private final RestTemplate kudagoRestTemplate;
    private final Semaphore kudagoSemaphore;

    public CategoryResponse[] getCategories() {
        try {
            kudagoSemaphore.acquire();
            try {
                return kudagoRestTemplate.getForObject(CATEGORIES_URL, CategoryResponse[].class);
            } finally {
                kudagoSemaphore.release();
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP error occurred while fetching categories: {}", e.getMessage());
        } catch (RestClientException e) {
            log.error("Network error occurred while fetching categories: {}", e.getMessage());
        } catch (InterruptedException e) {
            log.error("Interrupted while acquiring semaphore: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        return null;
    }

        public LocationResponse[] getLocations() {
            try {
                kudagoSemaphore.acquire();
                try {
                    return kudagoRestTemplate.getForObject(LOCATIONS_URL, LocationResponse[].class);
                } finally {
                    kudagoSemaphore.release();
                }
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                log.error("HTTP error occurred while fetching locations: {}", e.getMessage());
            } catch (RestClientException e) {
                log.error("Network error occurred while fetching locations: {}", e.getMessage());
            } catch (InterruptedException e) {
                log.error("Interrupted while acquiring semaphore: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }

            return null;
        }

        public EventKudagoResponse getEvents(long actualSince){
            String actualSinceQueryParam = "actual_since=" + actualSince;
            String fieldsQueryParam = "fields=title,price,dates,location";

            try {
                kudagoSemaphore.acquire();
                try {
                    return kudagoRestTemplate.getForObject(
                            EVENTS_URL + "?" + fieldsQueryParam + "&" + actualSinceQueryParam, EventKudagoResponse.class
                    );
                } finally {
                    kudagoSemaphore.release();
                }
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                log.error("HTTP error occurred while fetching events: {}", e.getMessage());
            } catch (RestClientException e) {
                log.error("Network error occurred while fetching events: {}", e.getMessage());
            } catch (InterruptedException e) {
                log.error("Interrupted while acquiring semaphore: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }

            return null;
        }

}