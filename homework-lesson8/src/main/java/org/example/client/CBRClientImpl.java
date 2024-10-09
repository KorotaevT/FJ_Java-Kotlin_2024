package org.example.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.example.dto.curs.ValCursDTO;
import org.example.dto.valute.ValuteFullDTO;
import org.example.exception.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CBRClientImpl implements CBRClient {

    private static final String DAILY_RATES_URL = "/scripts/XML_daily.asp";
    private static final String ALL_CURRENCIES = "/scripts/XML_valFull.asp";
    private static final Logger logger = LoggerFactory.getLogger(CBRClientImpl.class);

    private final RestTemplate cbrRestTemplate;

    @CircuitBreaker(name = "cbr", fallbackMethod = "fallbackMethodRate")
    @Cacheable(value = "currencyRates", key = "#root.methodName")
    public ValCursDTO getCurrencyRates() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String currentDate = "date_req=" + date.format(formatter);

        return cbrRestTemplate.getForObject(DAILY_RATES_URL + "?" + currentDate, ValCursDTO.class);
    }

    @CircuitBreaker(name = "cbr", fallbackMethod = "fallbackMethodCurrencies")
    @Cacheable(value = "currencyRates", key = "#root.methodName")
    public ValuteFullDTO getCurrencies() {
        return cbrRestTemplate.getForObject(ALL_CURRENCIES, ValuteFullDTO.class);
    }

    public ValCursDTO fallbackMethodRate(Throwable throwable) {
        logger.error("Error while calling getCurrencyRates: {}", throwable.getMessage(), throwable);
        throw new RuntimeException("CBR service is unavailable, unable to fetch currency rates", throwable);
    }

    public ValuteFullDTO fallbackMethodCurrencies(Throwable throwable) {
        logger.error("Error while calling getCurrencies: {}", throwable.getMessage(), throwable);
        throw new RuntimeException("CBR service is unavailable, unable to fetch the list of currencies", throwable);
    }

}