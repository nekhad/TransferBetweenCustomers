package com.example.testforme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyResponse {
    private String token;
    private String message;
    private String verificationCode;
    private boolean verified;
    public VerifyResponse(String meessage, String incorrectVerificationCode, String verificationCode, boolean verified, String email) {
        this.message = meessage;
    }

}