package com.compo.dto;

import com.compo.model.Currency;

import java.util.List;
import java.util.stream.Collectors;

public class Converter {

    public Converter() {
    }

    public static List<CurrencyDto> currencyListToCurrencyDtoList(final List<Currency> currencies){
        return currencies.stream().map(currency -> CurrencyDto.builder()
        .currencyId(currency.getCurrencyId())
        .currencyCode(currency.getCurrencyCode())
        .currencyDate(currency.getCurrencyDate())
        .currencyName(currency.getCurrencyName())
        .currencyRate(currency.getCurrencyRate())
        .build()).collect(Collectors.toList());
    }
}
