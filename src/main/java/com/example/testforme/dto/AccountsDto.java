package com.example.testforme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountsDto {
    private String id;
    private String accountNumber;
    private String expirationDate;
    private String cvc;
    private String isActive;
    private String currency;
    private String firstName;
    private String lastName;
}
