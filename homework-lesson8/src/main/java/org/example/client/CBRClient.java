package org.example.client;

import org.example.dto.curs.ValCursDTO;
import org.example.dto.valute.ValuteFullDTO;

public interface CBRClient {
    ValCursDTO getCurrencyRates();
    ValuteFullDTO getCurrencies();
}