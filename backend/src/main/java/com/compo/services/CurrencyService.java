package com.compo.services;

import com.compo.model.Currency;
import com.compo.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<Currency> findAll(){
        return currencyRepository.findAll();
    }

    public void save(Currency currency){
        if(currency != null){
            currencyRepository.save(currency);
        }
        System.out.println("Input log here");
    }



}
