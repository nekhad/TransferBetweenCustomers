package com.example.testforme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountsTransferDTO {
    private String toAccountNumber;
    private String fromAccountNumber;
    private String amount;
    private String currencyRate;
}
