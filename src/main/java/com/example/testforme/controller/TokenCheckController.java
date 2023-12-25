package com.example.testforme.controller;

import com.example.testforme.dto.AuthenticationResponse;
import com.example.testforme.repository.TokenRepository;
import com.example.testforme.security.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenCheckController {
    private final TokenRepository tokenRepository;

    @GetMapping("/check-token")
    public ResponseEntity<AuthenticationResponse> checkToken(@RequestParam("token") String token) {
        Optional<Token> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isPresent()) {
            Token t = optionalToken.get();
            if (t.isExpired() || t.isRevoked()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new AuthenticationResponse(null,"Token is expired or revoked",null, null));
            } else {
                return ResponseEntity.ok(new AuthenticationResponse("Token is valid"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthenticationResponse(null, "Invalid token",null,null));
        }
    }
}