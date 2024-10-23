package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.EventRequest;
import org.example.dto.response.EventKudagoDates;
import org.example.dto.response.EventKudagoResult;
import org.example.dto.response.EventResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.ServiceUnavailableException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final KudagoService kudagoService;

    private final CurrencyService currencyService;

    public CompletableFuture<List<EventResponse>> getEventsByCompletableFuture(EventRequest request) {
        var dateFrom = request.getDateFrom() != null ?
                request.getDateFrom() : LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        var dateTo = request.getDateTo() != null ?
                request.getDateTo() : LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        var dateFromTimestamp = dateFrom.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        var dateToTimestamp = dateTo.atStartOfDay().plusDays(1).toEpochSecond(ZoneOffset.UTC);

        var eventsFuture = CompletableFuture.supplyAsync(
                () -> kudagoService.getEvents(dateFromTimestamp).getResults()
        );
        var budgetInRublesFuture = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return convertBudgetToRubles(request.getBudget(), request.getCurrency());
                    } catch (ServiceUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        return eventsFuture.thenCombine(budgetInRublesFuture, (events, budgetInRubles) ->
                events.stream()
                        .filter(event -> checkDateForComplianceWithInterval(
                                event.getDates(),
                                dateFromTimestamp,
                                dateToTimestamp
                        ))
                        .peek(event -> removeUnnecessaryEventDates(event, dateFromTimestamp, dateToTimestamp))
                        .filter(event -> findMinPrice(event.getPrice()) <= budgetInRubles)
                        .map(this::eventKudagoResultDTOToEventResponseDTO)
                        .collect(Collectors.toList())
        );
    }

    public Flux<EventResponse> getEventsByMonoAndFlux(EventRequest request) {
        var dateFrom = request.getDateFrom() != null ?
                request.getDateFrom() : LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        var dateTo = request.getDateTo() != null ?
                request.getDateTo() : LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        var dateFromTimestamp = dateFrom.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        var dateToTimestamp = dateTo.atStartOfDay().plusDays(1).toEpochSecond(ZoneOffset.UTC);

        var eventsMono = Mono.fromCallable(() -> kudagoService.getEvents(dateFromTimestamp).getResults());
        var budgetInRublesMono = Mono.fromCallable(() -> convertBudgetToRubles(request.getBudget(), request.getCurrency()));

        return Mono.zip(eventsMono, budgetInRublesMono)
                .flatMapMany(tuple -> {
                    List<EventKudagoResult> events = (List<EventKudagoResult>) tuple.getT1();
                    Double budgetInRubles = tuple.getT2();

                    return Flux.fromIterable(events)
                            .filter(event -> checkDateForComplianceWithInterval(
                                    event.getDates(),
                                    dateFromTimestamp,
                                    dateToTimestamp
                            ))
                            .doOnNext(event -> removeUnnecessaryEventDates(event, dateFromTimestamp, dateToTimestamp))
                            .filter(event -> findMinPrice(event.getPrice()) <= budgetInRubles)
                            .map(this::eventKudagoResultDTOToEventResponseDTO);
                });
    }

    private Double convertBudgetToRubles(double budget, String currency) throws ServiceUnavailableException {
        return budget * getExchangeRate(currency.toUpperCase());
    }

    private double getExchangeRate(String currencyCode) throws ServiceUnavailableException {
        var dto = currencyService.getCurrencyRate(currencyCode);
        return dto.getRate();
    }

    private double findMinPrice(String text) {
        var patternString = "\\d*\\.?\\d+";

        var pattern = Pattern.compile(patternString);
        var matcher = pattern.matcher(text);

        var minimumPrice = 0;

        if (matcher.find()) {
            minimumPrice = (int) Double.parseDouble(matcher.group());
        }

        return minimumPrice;
    }

    private boolean checkDateForComplianceWithInterval(List<EventKudagoDates> dates, long dateFromTimestamp, long dateToTimestamp) {
        for (EventKudagoDates dto : dates) {
            if (dto.getStart() >= dateFromTimestamp && dto.getEnd() <= dateToTimestamp) {
                return true;
            }
        }

        return false;
    }

    private void removeUnnecessaryEventDates(EventKudagoResult responseDTO, long dateFromTimestamp, long dateToTimestamp) {
        List<EventKudagoDates> suitableDate = new ArrayList<>();

        for (EventKudagoDates dto : responseDTO.getDates()) {
            if (dto.getStart() >= dateFromTimestamp && dto.getEnd() <= dateToTimestamp) {
                suitableDate.add(dto);
                responseDTO.setDates(suitableDate);
                return;
            }
        }
    }

    private EventResponse eventKudagoResultDTOToEventResponseDTO(EventKudagoResult kudagoResultDTO) {
        var responseDTO = new EventResponse();
        responseDTO.setTitle(kudagoResultDTO.getTitle());
        responseDTO.setStartDate(
                LocalDateTime.ofInstant(Instant.ofEpochSecond(kudagoResultDTO.getDates().getFirst().getStart()), ZoneOffset.UTC)
        );
        responseDTO.setEndDate(
                LocalDateTime.ofInstant(Instant.ofEpochSecond(kudagoResultDTO.getDates().getFirst().getEnd()), ZoneOffset.UTC)
        );
        responseDTO.setPrice(findMinPrice(kudagoResultDTO.getPrice()));

        return responseDTO;
    }

}