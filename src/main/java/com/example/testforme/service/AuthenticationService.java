package com.example.testforme.service;

import com.example.testforme.dto.*;
import com.example.testforme.exception.NotFoundUser;
import com.example.testforme.exception.NotUniqueUser;
import com.example.testforme.exception.WrongPassword;
import com.example.testforme.repository.TokenRepository;
import com.example.testforme.repository.UserRepository;
import com.example.testforme.repository.VerificationRepository;
import com.example.testforme.security.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender;
    private final VerificationRepository verificationRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        if (repository.findAll().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(request.getEmail()))) {
            throw new NotUniqueUser();
        }
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
//                .verified(request.isVerified())
                .build();

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);

        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(null)
                .message("Successfully")
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .build();
    }

    public AuthenticationResponse sendCodeToEmail(SendCodeAgainRequest sendCodeAgainRequest) {
        var user = repository.findByEmail(sendCodeAgainRequest.getEmail())
                .orElseThrow();
        var verification = Verification.builder()
                .user(user)
                .verificationCode(generateVerificationCode())
                .status("A")
                .build();

        verificationRepository.save(verification);
//        sendCodeAgainRequest.setStatus("D");
        user.setEmail(sendCodeAgainRequest.getEmail());
        System.out.println("asasas");
        user.setVerified(false);
        System.out.println("sendVerificationCode(user.getId())" +"Start OLDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
        sendVerificationCode(user.getId());


        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = () -> {
            verification.setStatus("D");
            verificationRepository.save(verification);
        };
        service.scheduleAtFixedRate(runnable,1,1, TimeUnit.MINUTES);


        return AuthenticationResponse.builder()
                .token(null)
                .message("----------")
                .build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var userByEmail = repository.findByEmail(request.getEmail())
                .orElseThrow(NotFoundUser::new);
        if (!passwordEncoder.matches(request.getPassword(), userByEmail.getPassword())) {
            throw new WrongPassword();
        }
//        if(!userByEmail.isVerified()){
//            throw new NotVerified();
//        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    public void sendVerificationCode(String userId) {
        User user = repository.findUserById(userId).orElse(null);
        if (user != null && !user.isVerified()) {
            sendEmail(user.getEmail(), verificationRepository.findVerificationCodesWithStatusA());
        }
    }
    @Transactional
    public VerifyResponse verifyUser(String email, String verificationCode) {
        User user = repository.findByEmail(email).orElse(null);
        Verification verification = verificationRepository.findByVerificationCode(verificationCode).orElse(null);
        String verification1 = verificationRepository.findVerificationCodesWithStatusAB(verificationCode);
        if (user != null && verification1.equals(verificationCode)) {
            user.setVerified(true);
            repository.save(user);
            assert verification != null;
            verificationRepository.save(verification);
        }
        assert verification != null;
        assert user != null;
        return VerifyResponse.builder()
                .token(null)
                .message("Verified")
                .verificationCode(verification.getVerificationCode())
                .verified(user.isVerified())
                .build();
    }
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
    private void sendEmail(String email, String verificationCode) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(email);
            helper.setSubject("Email Verification Code");
            helper.setText("Your verification code is: " + verificationCode);
            System.out.println("salaam");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}