package com.example.testforme.repository;

import com.example.testforme.security.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {

    @Query(value = """
            select t from Token t
            inner join User u\s on t.user.id = u.id\s
            where u.verified = true and u.id = :id and (t.expired = false and t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(String id);

    Optional<Token> findByToken(String token);
}