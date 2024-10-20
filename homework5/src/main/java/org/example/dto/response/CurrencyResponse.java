package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyResponse {
    private String currency;
    private Double rate;
}