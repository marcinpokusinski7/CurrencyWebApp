package com.compo.currencyJobs;

import com.compo.model.Currency;
import com.compo.pojo.CurrencyTable;
import com.compo.pojo.Rate;
import com.compo.services.CurrencyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class NbpApiReader {

    private final CurrencyService currencyService;

    @Autowired
    public NbpApiReader(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    private List<CurrencyTable> readCurrencyRates() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<CurrencyTable> currencyCollection = new ArrayList<>();
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            HttpClient clientNbp = HttpClient.newBuilder()
                    .executor(executorService)
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            List<URI> targetTables = Arrays.asList(
                    new URI("https://api.nbp.pl/api/exchangerates/tables/A?format=json"),
                    new URI("https://api.nbp.pl/api/exchangerates/tables/B?format=json")
            );
            List<CompletableFuture<String>> tablesResult = targetTables.stream()
                    .map(url -> clientNbp.sendAsync(
                            HttpRequest.newBuilder(url)
                                    .GET()
                                    .setHeader("accept", "application/json")
                                    .build(),
                            HttpResponse.BodyHandlers.ofString())
                            .thenApply(HttpResponse::body))
                    .collect(Collectors.toList());

            for (CompletableFuture<String> table : tablesResult) {
                currencyCollection.addAll(mapper.readValue(table.get(), new TypeReference<List<CurrencyTable>>(){}));
            }
            return currencyCollection;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readCurrencyTable() {
        try {
            String currencyDate;
            for(CurrencyTable multipleTables: readCurrencyRates()){
                currencyDate = multipleTables.getEffectiveDate();
                currencyDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
                if (currencyDate == null) {
                    return Instant.now().toString();
                } else {
                    return currencyDate;
                }
            }
        } catch (NullPointerException nullPointerException) {
            System.out.println("date was null");
        }
        return null;
    }

    public List<Currency> convertRateToCurrencyRecord() {
        String currencyDate = readCurrencyTable();
        List<CurrencyTable> currencyTable = readCurrencyRates();
        List<Currency> listOfRates = new ArrayList<>();
        for (CurrencyTable multipleTables : currencyTable) {
            for(Rate rates : multipleTables.getRates()){
            Currency currencyRecord = new Currency();
            currencyRecord.setCurrencyCode(rates.getCode());
            currencyRecord.setCurrencyRate(rates.getMid());
            currencyRecord.setCurrencyName(rates.getCurrency());
            currencyRecord.setCurrencyDate(currencyDate);
            listOfRates.add(currencyRecord);
            }
        }
        return listOfRates;
    }

}
