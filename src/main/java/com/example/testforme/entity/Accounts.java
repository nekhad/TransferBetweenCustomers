package com.example.testforme.entity;


import com.example.testforme.security.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_account")
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "expiration_date")
    private String expirationDate;

    @Column(name = "cvc")
    private String cvc;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "balance")
    private double balance;

    @Column(name = "currency")
    private String currency;

    @Column(name = "status")
    private String status;

    @Column(name = "currency_rate")
    private String currencyRate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
