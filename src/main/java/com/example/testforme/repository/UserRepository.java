package com.example.testforme.repository;

import com.example.testforme.security.Token;
import com.example.testforme.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.id = :id")
    Optional<User> findUserById(String id);

    @Query("select u from User u inner join Token t on t.user.id = u.id where u.verified = true and t.token = :token")
    User getUserByToken(String token);


}