package com.example.testforme.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CurrencyRequestDTO {

    private String id;
    private String currencyType;
    private BigDecimal rate;

}
