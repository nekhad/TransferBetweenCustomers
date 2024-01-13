package com.example.testforme.service;

import com.example.testforme.entity.Currency;
import com.example.testforme.dto.CurrencyResponseDTO;
import com.example.testforme.exception.ErrorMessage;
import com.example.testforme.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    @Value("${currency.api.url}")
    private String url;

    private final RestTemplate restTemplate;
    private final CurrencyRepository currencyRepository;

    public CurrencyResponseDTO allCurrencyRates() {
        return restTemplate.getForObject(url, CurrencyResponseDTO.class);
    }

    public double getSpecificExchangeRate(String sourceCurrency, String targetCurrency, LocalDate date) {
        CurrencyResponseDTO response = restTemplate.getForObject(url, CurrencyResponseDTO.class);
        if (response == null) {
            throw new IllegalArgumentException(HttpStatus.BAD_REQUEST.name());
        }
        double sourceRate = response.getRates().get(sourceCurrency);
        double targetRate = response.getRates().get(targetCurrency);
        if (sourceRate == 0 || targetRate == 0) {
            throw new IllegalArgumentException(HttpStatus.BAD_REQUEST.name());
        }
        Currency currency = new Currency();
        currency.setCurrencyType(sourceCurrency);
        currency.setRate(targetRate / sourceRate);
        currency.setUpdatedDate(LocalDateTime.now());
        currencyRepository.save(currency);
        return targetRate / sourceRate;
    }

    public double convertAmount(double amount, String source, String target, LocalDate date) {
        double exchangeRate = getSpecificExchangeRate(source, target, date);
        if (exchangeRate == 0) {
            throw new IllegalArgumentException(HttpStatus.BAD_REQUEST.name());
        }
        return amount * exchangeRate;
    }

    public void getRecentCurrentData(CurrencyResponseDTO currencyResponseDTO) {
        LocalDateTime now = LocalDateTime.now().minusSeconds(30);
        List<Currency> reminders = currencyRepository.findByUpdatedDateBefore(now);
        if(reminders.isEmpty()){
            List<Currency> currencies = new ArrayList<>();
            for (String key: currencyResponseDTO.getRates().keySet()) {
                Currency currency = new Currency();
                currency.setUpdatedDate(LocalDateTime.now());
                currency.setCurrencyType(key);
                currency.setRate(currencyResponseDTO.getRates().get(key));
                currencies.add(currency);
            }
            currencyRepository.saveAll(currencies);

        }
        for (Currency currency : reminders) {
            currency.setUpdatedDate(LocalDateTime.now());
            currency.setCurrencyType(currencyResponseDTO.getCurrencyType());
            currency.setRate(currencyResponseDTO.getRates().get("rates"));
            currencyRepository.save(currency);
        }
    }

}