package com.example.testforme.controller;

import com.example.testforme.dto.*;
import com.example.testforme.repository.CurrencyRepository;
import com.example.testforme.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyRepository currencyRepository;

    @GetMapping("/latest")
    public ResponseEntity<CurrencyResponseDTO> currencyRates() {
        return ResponseEntity.status(HttpStatus.OK).body(currencyService.allCurrencyRates());
    }
    @GetMapping("/read")
    public ResponseEntity<List<CurrencySelectBoxDTO>> getAllCurrencyRates() {
        CurrencyResponseDTO currencyResponseDTO = currencyService.allCurrencyRates();
        List<CurrencySelectBoxDTO> currencySelectBoxDTOList = currencyService.mapToCurrencySelectBoxDTO(currencyResponseDTO);
        return new ResponseEntity<>(currencySelectBoxDTOList, HttpStatus.OK);
    }

    @GetMapping("/get-currency")
    public ResponseEntity<List<CurrencyReadDTO>> getAllCurrencies() {
        CurrencyResponseDTO currencyResponseDTO = currencyService.allCurrencyRates();
        List<CurrencyReadDTO> currencyReadDTOS = currencyService.mapToCurrencyReadDTO(currencyResponseDTO);
        return new ResponseEntity<>(currencyReadDTOS, HttpStatus.OK);
    }

    @GetMapping("/convert")
    public ResponseEntity<Double> convertAmount(@RequestParam Double amount, @RequestParam String source,
                                                @RequestParam String target, @RequestParam LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(currencyService.convertAmount(amount, source, target, date));
    }

    @GetMapping("/specific")
    public ResponseEntity<Double> getExchangeRate(@RequestParam String source, @RequestParam String target,
                                                  @RequestParam LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(currencyService.getSpecificExchangeRate(source, target, date));
    }

}
