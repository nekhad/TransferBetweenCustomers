package com.example.testforme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountsRequestDTO {
    private String id;
    private String accountNumber;
    private String expirationDate;
    private String cvc;
    private String isActive;
    private String currency;
}
