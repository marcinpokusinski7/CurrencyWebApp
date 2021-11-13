package com.compo.controllers;

import com.compo.dto.CurrencyDto;
import com.compo.model.Currency;
import com.compo.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.compo.dto.Converter.currencyListToCurrencyDtoList;

@RestController
@RequestMapping(value = "/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @GetMapping()
    public ResponseEntity<List<CurrencyDto>> getCurrencies(){
        List<Currency> currencyList = currencyService.findAll();
        return new ResponseEntity<>(currencyListToCurrencyDtoList(currencyList), HttpStatus.OK);
    }
}
