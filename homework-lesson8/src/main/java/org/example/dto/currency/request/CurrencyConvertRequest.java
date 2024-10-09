package org.example.dto.currency.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyConvertRequest {

    @NotBlank(message = "the fromCurrency field could not be blank")
    private String fromCurrency;

    @NotBlank(message = "the toCurrency field could not be blank")
    private String toCurrency;

    @NotNull(message = "the amount field must not be null")
    @Positive(message = "the amount field must be positive")
    private Double amount;
}