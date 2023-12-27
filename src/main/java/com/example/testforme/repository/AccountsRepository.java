package com.example.testforme.repository;

import com.example.testforme.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, String> {

    boolean existsByAccountNumber(String accountNumber);
    // get accounts of user
    @Query(value = "select a from Accounts a inner join Token t on a.user.id = t.user.id where t.token = ?1")
    List<Accounts> getAccountsOfUser(String token);

//    @Query("SELECT a FROM Accounts a JOIN a.user u JOIN Token t ON u.id = t.user.id WHERE t.token = ?1")
//    List<Accounts> getAccountsByToken(String token);
}