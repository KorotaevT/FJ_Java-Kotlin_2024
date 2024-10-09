package org.example.service;

import org.example.dto.currency.request.CurrencyConvertRequest;
import org.example.dto.currency.response.CurrencyConvertResponse;
import org.example.dto.currency.response.CurrencyRateResponse;
import org.example.dto.curs.ValCursDTO;
import org.example.dto.item.ItemDTO;
import org.example.dto.valute.ValuteDTO;
import org.example.dto.valute.ValuteFullDTO;
import org.example.exception.CurrencyRateNotFoundException;
import org.example.exception.NonexistentCurrencyException;
import org.example.client.CBRClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CBRClient cbrClient;

    @InjectMocks
    private CurrencyService currencyService;

    @ParameterizedTest
    @CsvSource({
            "USD, 95.00, 95.00",
            "EUR, 105.00, 105.00"
    })
    void getCurrencyRateByCode_ValidCode_ReturnsRate(String code, String vUnitRate, Double expectedVUnitRate) {
        ValCursDTO valCursDTO = new ValCursDTO();
        ValuteDTO valuteDTO = createValuteDTO(code, vUnitRate);
        valCursDTO.setValutes(List.of(valuteDTO));

        ValuteFullDTO valuteFullDTO = new ValuteFullDTO();
        ItemDTO usdItemDTO = createItemDTO(code);
        valuteFullDTO.setItems(List.of(usdItemDTO));

        when(cbrClient.getCurrencies()).thenReturn(valuteFullDTO);
        when(cbrClient.getCurrencyRates()).thenReturn(valCursDTO);

        CurrencyRateResponse response = currencyService.getCurrencyRateByCode(code);

        assertEquals(code, response.getCurrency());
        assertEquals(expectedVUnitRate, response.getRate());
    }

    @Test
    void getCurrencyRateByCode_ValidCodeButRateNotFound_ThrowsException() {
        String code = "ATS";
        ValCursDTO valCursDTO = new ValCursDTO();
        ValuteDTO usdValuteDTO = createValuteDTO("USD", "95.00");
        valCursDTO.setValutes(List.of(usdValuteDTO));

        ValuteFullDTO valuteFullDTO = new ValuteFullDTO();
        ItemDTO usdItemDTO = createItemDTO("USD");
        ItemDTO atsItemDTO = createItemDTO("ATS");
        valuteFullDTO.setItems(List.of(usdItemDTO, atsItemDTO));

        when(cbrClient.getCurrencies()).thenReturn(valuteFullDTO);
        when(cbrClient.getCurrencyRates()).thenReturn(valCursDTO);

        assertThrows(CurrencyRateNotFoundException.class, () -> currencyService.getCurrencyRateByCode(code));
    }

    @Test
    void getCurrencyRateByCode_InvalidCode_ThrowsException() {
        String code = "INVALID";

        ValuteFullDTO valuteFullDTO = new ValuteFullDTO();
        ItemDTO usdItemDTO = createItemDTO("USD");
        valuteFullDTO.setItems(List.of(usdItemDTO));

        when(cbrClient.getCurrencies()).thenReturn(valuteFullDTO);

        assertThrows(NonexistentCurrencyException.class, () -> currencyService.getCurrencyRateByCode(code));
    }

    @ParameterizedTest
    @CsvSource({
            "USD, EUR, 100.0, 90.476",
            "EUR, USD, 100.0, 110.526"
    })
    void convertCurrency_ValidRequest_ReturnsConvertedAmount(
            String fromCurrency, String toCurrency, double amount, double expectedConvertedAmount
    ) {
        ValCursDTO valCursDTO = new ValCursDTO();
        ValuteDTO usdValuteDTO = createValuteDTO("USD", "95.00");
        ValuteDTO eurValuteDTO = createValuteDTO("EUR", "105.00");
        valCursDTO.setValutes(List.of(usdValuteDTO, eurValuteDTO));

        ValuteFullDTO valuteFullDTO = new ValuteFullDTO();
        ItemDTO usdItemDTO = createItemDTO("USD");
        ItemDTO eurItemDTO = createItemDTO("EUR");
        valuteFullDTO.setItems(List.of(usdItemDTO, eurItemDTO));

        when(cbrClient.getCurrencyRates()).thenReturn(valCursDTO);
        when(cbrClient.getCurrencies()).thenReturn(valuteFullDTO);

        CurrencyConvertRequest requestDTO = createCurrencyConvertRequestDTO(fromCurrency, toCurrency, amount);

        CurrencyConvertResponse responseDTO = currencyService.convertCurrency(requestDTO);

        assertEquals(fromCurrency, responseDTO.getFromCurrency());
        assertEquals(toCurrency, responseDTO.getToCurrency());
        assertEquals(expectedConvertedAmount, responseDTO.getConvertedAmount(), 0.01);
    }

    @Test
    void convertCurrency_InvalidFromCurrency_ThrowsException() {
        CurrencyConvertRequest requestDTO = createCurrencyConvertRequestDTO("INVALID", "USD", 100.0);

        ValuteFullDTO valuteFullDTO = new ValuteFullDTO();
        valuteFullDTO.setItems(List.of());

        when(cbrClient.getCurrencies()).thenReturn(valuteFullDTO);

        assertThrows(NonexistentCurrencyException.class, () -> currencyService.convertCurrency(requestDTO));
    }

    @Test
    void convertCurrency_ValidFromCurrencyButRateNotFound_ThrowsException() {
        CurrencyConvertRequest requestDTO = createCurrencyConvertRequestDTO("ATS", "USD", 100.0);

        ValuteFullDTO valuteFullDTO = new ValuteFullDTO();
        ItemDTO usdItemDTO = createItemDTO("USD");
        ItemDTO atsItemDTO = createItemDTO("ATS");
        valuteFullDTO.setItems(List.of(usdItemDTO, atsItemDTO));

        ValCursDTO valCursDTO = new ValCursDTO();
        ValuteDTO usdValuteDTO = createValuteDTO("USD", "95.00");
        valCursDTO.setValutes(List.of(usdValuteDTO));

        when(cbrClient.getCurrencies()).thenReturn(valuteFullDTO);
        when(cbrClient.getCurrencyRates()).thenReturn(valCursDTO);

        assertThrows(CurrencyRateNotFoundException.class, () -> currencyService.convertCurrency(requestDTO));
    }

    @Test
    void convertCurrency_InvalidToCurrency_ThrowsException() {
        CurrencyConvertRequest requestDTO = createCurrencyConvertRequestDTO("USD", "INVALID", 100.0);

        ValuteFullDTO valuteFullDTO = new ValuteFullDTO();
        valuteFullDTO.setItems(List.of());

        when(cbrClient.getCurrencies()).thenReturn(valuteFullDTO);

        assertThrows(NonexistentCurrencyException.class, () -> currencyService.convertCurrency(requestDTO));
    }

    @Test
    void convertCurrency_ValidToCurrencyButRateNotFound_ThrowsException() {
        CurrencyConvertRequest requestDTO = createCurrencyConvertRequestDTO("USD", "ATS", 100.0);

        ValuteFullDTO valuteFullDTO = new ValuteFullDTO();
        ItemDTO usdItemDTO = createItemDTO("USD");
        ItemDTO atsItemDTO = createItemDTO("ATS");
        valuteFullDTO.setItems(List.of(usdItemDTO, atsItemDTO));

        ValCursDTO valCursDTO = new ValCursDTO();
        ValuteDTO usdValuteDTO = createValuteDTO("USD", "95.00");
        valCursDTO.setValutes(List.of(usdValuteDTO));

        when(cbrClient.getCurrencies()).thenReturn(valuteFullDTO);
        when(cbrClient.getCurrencyRates()).thenReturn(valCursDTO);

        assertThrows(CurrencyRateNotFoundException.class, () -> currencyService.convertCurrency(requestDTO));
    }

    private ValuteDTO createValuteDTO(String charCode, String vUnitRate) {
        ValuteDTO valuteDTO = new ValuteDTO();
        valuteDTO.setCharCode(charCode);
        valuteDTO.setVUnitRate(vUnitRate);

        return valuteDTO;
    }

    private ItemDTO createItemDTO(String isoCharCode) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setIsoCharCode(isoCharCode);

        return itemDTO;
    }

    private CurrencyConvertRequest createCurrencyConvertRequestDTO(String fromCurrency, String toCurrency, Double amount) {
        CurrencyConvertRequest requestDTO = new CurrencyConvertRequest();
        requestDTO.setFromCurrency(fromCurrency);
        requestDTO.setToCurrency(toCurrency);
        requestDTO.setAmount(amount);

        return requestDTO;
    }

}