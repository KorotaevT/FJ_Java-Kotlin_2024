package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.EventDetailsRequest;
import org.example.dto.request.EventRequest;
import org.example.dto.response.EventEntityResponse;
import org.example.dto.response.EventResponse;
import org.example.service.EventService;
import org.example.timed.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Timed
public class EventController {

    private final EventService eventService;

    @GetMapping("/byCompletableFuture")
    public CompletableFuture<ResponseEntity<List<EventResponse>>> getEventsByCompletableFuture(
            @RequestParam double budget,
            @RequestParam String currency,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {

        EventRequest request = new EventRequest();
        request.setBudget(budget);
        request.setCurrency(currency);

        if (dateFrom != null) {
            request.setDateFrom(LocalDate.parse(dateFrom));
        }

        if (dateTo != null) {
            request.setDateTo(LocalDate.parse(dateTo));
        }

        return eventService.getEventsByCompletableFuture(request).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/byMonoAndFlux")
    public Mono<ResponseEntity<Flux<EventResponse>>> getEventsByMonoAndFlux(
            @RequestParam double budget,
            @RequestParam String currency,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {

        EventRequest request = new EventRequest();
        request.setBudget(budget);
        request.setCurrency(currency);

        if (dateFrom != null) {
            request.setDateFrom(LocalDate.parse(dateFrom));
        }

        if (dateTo != null) {
            request.setDateTo(LocalDate.parse(dateTo));
        }

        Flux<EventResponse> eventsFlux = eventService.getEventsByMonoAndFlux(request);

        return Mono.just(ResponseEntity.ok(eventsFlux));
    }

    @GetMapping
    public List<EventEntityResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventEntityResponse getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/filter")
    public List<EventEntityResponse> getEvents(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "place", required = false) String placeName,
            @RequestParam(value = "fromDate", required = false) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) LocalDate toDate
    ) {
        return eventService.getEvents(name, placeName, fromDate, toDate);
    }

    @PostMapping
    public EventEntityResponse createEvent(@Valid @RequestBody EventDetailsRequest eventDetails) {
        return eventService.createEvent(eventDetails);
    }

    @PutMapping("/{id}")
    public EventEntityResponse updateEvent(@PathVariable Long id, @Valid @RequestBody EventDetailsRequest eventDetails) {
        return eventService.updateEvent(id, eventDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

}