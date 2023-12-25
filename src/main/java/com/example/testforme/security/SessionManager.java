package com.example.testforme.security;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
@Data
public class SessionManager {
    private String userName;

    private String firstName;

    private String lastName;
}