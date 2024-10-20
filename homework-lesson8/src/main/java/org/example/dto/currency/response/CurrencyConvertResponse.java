package org.example.dto.currency.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyConvertResponse {
    private String fromCurrency;
    private String toCurrency;
    private Double convertedAmount;
}