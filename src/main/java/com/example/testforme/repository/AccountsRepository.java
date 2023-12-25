package com.example.testforme.repository;

import com.example.testforme.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, String> {

    boolean existsByAccountNumber(String accountNumber);
    // get accounts of user
    List<Accounts> getAccountsByUserId(String userId);
}
