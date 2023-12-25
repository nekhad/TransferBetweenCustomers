package com.example.testforme.security;


import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "verifications")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Getter
    @Column(name = "verification_code")
    private String verificationCode;

    @Getter
    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;



    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

}
