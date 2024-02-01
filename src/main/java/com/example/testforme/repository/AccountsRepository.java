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
            "where a.isActive = 'A' and t.token = :token")
    List<Accounts> getActiveAccountsOfOwners(String token);

    @Query("select a from Accounts a inner join Token t on a.user.id = t.user.id " +
            "where a.isActive != 'A' and t.token = :token")
    List<Accounts> getNonActiveAccountsOfOwners(String token);

    Accounts getByAccountNumber(String accountNumber);

    @Query("select c.rate from Currency c inner join Accounts a on c.currencyType = a.currencyRate " +
            "where a.isActive = 'A' and a.accountNumber = :accountNumber")
    String getCurrencyRateByAccountNumber(String accountNumber);
}