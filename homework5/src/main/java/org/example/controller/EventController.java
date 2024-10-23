package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.EventRequest;
import org.example.dto.response.EventResponse;
import org.example.service.EventService;
import org.example.timed.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}