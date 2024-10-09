package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.dto.currency.request.CurrencyConvertRequest;
import org.example.dto.currency.response.CurrencyConvertResponse;
import org.example.dto.currency.response.CurrencyRateResponse;
import org.example.service.CurrencyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "CurrencyController"
)
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("rates/{code}")
    @Operation(description = "Getting the currency exchange rate by its code")
    @ApiResponses({
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Currency rate not found"),
                    @ApiResponse(responseCode = "503", description = "Service unavailable")
            })
    public CurrencyRateResponse getCurrencyRateByRate(@NotBlank(message = "the code variable could not be blank") @PathVariable String code) {
        return currencyService.getCurrencyRateByCode(code);
    }

    @PostMapping("/convert")
    @Operation(description = "Getting a list of user recipes")
    @ApiResponses({
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Currency rate not found"),
                    @ApiResponse(responseCode = "503", description = "Service unavailable")
            })
    public CurrencyConvertResponse convertCurrency(@Valid @RequestBody CurrencyConvertRequest dto) {
        return currencyService.convertCurrency(dto);
    }

}