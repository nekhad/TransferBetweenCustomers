package com.example.testforme.controller;


import com.example.testforme.dto.*;
import com.example.testforme.exception.NotFoundUser;
import com.example.testforme.exception.NotUniqueUser;
import com.example.testforme.exception.WrongPassword;
import com.example.testforme.repository.TokenRepository;
import com.example.testforme.security.Token;
import com.example.testforme.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    private final TokenRepository tokenRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(service.register(request));
        } catch (NotUniqueUser ex) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new AuthenticationResponse(null, "This email is already exist", null, null));
        }
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (WrongPassword ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthenticationResponse(null, "Wrong password", null, null));
        } catch (NotFoundUser ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthenticationResponse(null, "No such e-mail address was found", null, null));
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<VerifyResponse> verify(@RequestBody VerifyRequest request) {
        try {
            return ResponseEntity.ok(service.verifyUser(request.getEmail(),request.getVerificationCode()));
        } catch (WrongPassword ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new VerifyResponse(null, "Incorrect verification code", request.getVerificationCode(), request.isVerified(),request.getEmail()));
        }
    }


    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Optional<Token> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            Token token2 = tokenOptional.get();
            if (!(token2.isExpired() && token2.isRevoked())) {
                token2.setExpired(true);
                token2.setRevoked(true);
                tokenRepository.save(token2);
                SecurityContextHolder.clearContext();
            }
        }
    }

}