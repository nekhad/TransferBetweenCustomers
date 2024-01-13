package com.example.testforme.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Setter
@Getter
public class CurrencyResponseDTO {

    private String id;
    private String currencyType;
    private Map<String, Double> rates;

}