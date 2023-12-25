package com.example.testforme.security;


import com.example.testforme.entity.Accounts;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "_user")
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 20, message = "Username can be a minimum of 2 and a maximum of 20 characters")
    String firstname;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 20, message = "Username can be a minimum of 2 and a maximum of 20 characters")
    String lastname;

    @NotBlank(message = "E-mail cannot be empty")
    @Email(message = "Invalid e-mail address")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "verified")
    private boolean verified;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "user")
    private List<Verification> verifications;

//    @Getter(AccessLevel.NONE)
//    @EqualsAndHashCode.Exclude
//    @ToStringExclude
//    @OneToMany(mappedBy = "user")
//    Collection<Car> cars;

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "user")
    private List<Accounts> accounts;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}