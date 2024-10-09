package org.example.dto.currency.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyRateResponse {
    private String currency;
    private Double rate;
}