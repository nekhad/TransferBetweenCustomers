package com.example.testforme.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class CurrencySelectBoxDTO {
    private String id;
    private String currencyType;
}
