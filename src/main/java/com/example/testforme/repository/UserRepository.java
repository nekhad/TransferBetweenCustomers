package com.example.testforme.repository;

import com.example.testforme.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);
    @Query("select u from User u where u.id = :id")
    Optional<User> findUserById(String id);


    @Query("select u from User u inner join Token t on t.user.id = u.id where t.token = :token")
    User getUserByToken(String token);
}