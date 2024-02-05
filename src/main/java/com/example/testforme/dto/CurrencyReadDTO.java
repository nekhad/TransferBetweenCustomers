package com.example.testforme.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class CurrencyReadDTO {
    private double rate;
    private String currencyType;
}