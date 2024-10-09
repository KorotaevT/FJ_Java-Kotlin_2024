package org.example.service;

import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private static final String RUSSIAN_RUBLE_CODE = "RUB";

    private final CBRClient cbrClient;

    public CurrencyRateResponse getCurrencyRateByCode(String code) {
        if (!doesSuchCurrencyExist(code)) {
            throw new NonexistentCurrencyException(getNonexistentCurrencyExceptionMessage(code));
        }

        double rate = getCurrencyRateFromCBR(code);

        CurrencyRateResponse dto = new CurrencyRateResponse();
        dto.setCurrency(code);
        dto.setRate(rate);

        return dto;
    }

    private Double getCurrencyRateFromCBR(String code) {
        if (code.equals(RUSSIAN_RUBLE_CODE)) {
            return 1.;
        }

        ValCursDTO valCursDTO = cbrClient.getCurrencyRates();

        for (ValuteDTO valuteDTO : valCursDTO.getValutes()) {
            if (valuteDTO.getCharCode().equals(code)) {
                return getDoubleRate(valuteDTO.getVUnitRate());
            }
        }

        throw new CurrencyRateNotFoundException("Rate for currency with code \"" + code + "\" not found");
    }

    public CurrencyConvertResponse convertCurrency(CurrencyConvertRequest requestDTO) {
        if (!(doesSuchCurrencyExist(requestDTO.getFromCurrency()))) {
            throw new NonexistentCurrencyException(
                    getNonexistentCurrencyExceptionMessage(requestDTO.getFromCurrency())
            );
        }

        if (!doesSuchCurrencyExist(requestDTO.getToCurrency())) {
            throw new NonexistentCurrencyException(
                    getNonexistentCurrencyExceptionMessage(requestDTO.getToCurrency())
            );
        }

        double rateFromCurrency = getCurrencyRateFromCBR(requestDTO.getFromCurrency());
        double rateToCurrency = getCurrencyRateFromCBR(requestDTO.getToCurrency());

        double convertedAmount = rateFromCurrency * requestDTO.getAmount() / rateToCurrency;

        CurrencyConvertResponse responseDTO = new CurrencyConvertResponse();
        responseDTO.setFromCurrency(requestDTO.getFromCurrency());
        responseDTO.setToCurrency(requestDTO.getToCurrency());
        responseDTO.setConvertedAmount(convertedAmount);

        return responseDTO;
    }

    private boolean doesSuchCurrencyExist(String code) {
        if (code.equals(RUSSIAN_RUBLE_CODE)) {
            return true;
        }

        ValuteFullDTO allCurrency = cbrClient.getCurrencies();

        for (ItemDTO itemDTO : allCurrency.getItems()) {
            if (itemDTO.getIsoCharCode().equals(code)) {
                return true;
            }
        }

        return false;
    }

    private Double getDoubleRate(String rate) {
        return Double.valueOf(rate.replace(',', '.'));
    }

    private String getNonexistentCurrencyExceptionMessage(String code) {
        return "Currency with code " + code + " doesn't exist";
    }

}