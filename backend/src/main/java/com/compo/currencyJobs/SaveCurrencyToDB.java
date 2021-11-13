package com.compo.currencyJobs;

import com.compo.model.Currency;
import com.compo.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SaveCurrencyToDB {

    private final CurrencyService currencyService;
    private final NbpApiReader nbpApiReader;

    @Autowired
    public SaveCurrencyToDB(CurrencyService currencyService, NbpApiReader nbpApiReader) {
        this.currencyService = currencyService;
        this.nbpApiReader = nbpApiReader;
    }

    @PostConstruct
    private void callAsynchronousApi() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::saveToDatabase, 0, 1, TimeUnit.DAYS);
    }

    private void saveToDatabase() {
        List<Currency> currencies = nbpApiReader.convertRateToCurrencyRecord();
        if (currencies != null) {
            for (Currency oneCurrency : currencies) {
                currencyService.save(oneCurrency);
            }
        } else {
            System.out.println("Empty");
        }
    }
}
