package com.example.testforme.repository;

import com.example.testforme.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, String> {

    boolean existsByAccountNumber(String accountNumber);

    @Query("select a from Accounts a inner join Token t on a.user.id = t.user.id " +
            "where a.isActive = 'active' and t.token = :token")
    List<Accounts> getActiveAccountsOfOwners(String token);

    @Query("select a from Accounts a inner join Token t on a.user.id = t.user.id " +
            "where a.isActive != 'active' and t.token = :token")
    List<Accounts> getNonActiveAccountsOfOwners(String token);

    Accounts getByAccountNumber(String accountNumber);

    @Query("select a.currencyRate from Accounts a " +
            "where a.isActive = 'active' and a.accountNumber = :accountNumber")
    String getCurrencyRateByAccountNumber(String accountNumber);
}