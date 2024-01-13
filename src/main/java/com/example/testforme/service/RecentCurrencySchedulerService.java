package com.example.testforme.service;

import com.example.testforme.dto.CurrencyResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecentCurrencySchedulerService {

    private final CurrencyService currencyService;

    @Scheduled(cron = "0/30 * * * * *")
    public void recentCurrencyData() {
        log.info("Inside recentCurrencyData : ");

        CurrencyResponseDTO currencyResponseDTO = currencyService.allCurrencyRates();
        log.info(currencyResponseDTO.toString());
        currencyService.getRecentCurrentData(currencyResponseDTO);
    }

}
