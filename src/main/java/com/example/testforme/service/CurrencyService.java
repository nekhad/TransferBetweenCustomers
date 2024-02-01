package com.example.testforme.service;

import com.example.testforme.entity.Currency;
import com.example.testforme.dto.CurrencyResponseDTO;
import com.example.testforme.exception.ErrorMessage;
import com.example.testforme.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
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
        LocalDateTime now = LocalDateTime.now().minusSeconds(70);
        LocalDateTime now1 = LocalDateTime.now();
        log.info("Before 70 seconds from now" + now);
        log.info("Real Now" + now1);
        List<Currency> reminders = currencyRepository.findByUpdatedDateBetween(now,now1);
        System.out.println(reminders + "results");
        log.info("reminders" + reminders);
        if (reminders.isEmpty()) {
        List<Currency> currencies = new ArrayList<>();
        for (String key : currencyResponseDTO.getRates().keySet()) {
            Currency currency = new Currency();
            currency.setUpdatedDate(LocalDateTime.now());
            currency.setCurrencyType(key);
            currency.setRate(Math.round(currencyResponseDTO.getRates().get(key) * 10.0) / 10.0);
            currencies.add(currency);
        }
        currencyRepository.saveAll(currencies);

        } else {
            for (Currency currency : reminders) {
                currency.setUpdatedDate(LocalDateTime.now());
                currencyRepository.save(currency);
                log.info("update loading...");
            }
    }

    }
}
