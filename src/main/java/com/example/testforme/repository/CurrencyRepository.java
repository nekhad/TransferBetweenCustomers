package com.example.testforme.repository;

import com.example.testforme.dto.CurrencyReadDTO;
import com.example.testforme.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, String> {

    List<Currency> findByUpdatedDateBetween(LocalDateTime localDateTime, LocalDateTime localDateTime2);

    @Query("select c.rate from Currency c where c.currencyType = :currency")
    String getCurrencyRateByCurrencyId(String currency);

    @Query("select c.currencyType from Currency c where c.id = :id")
    String getCurrencyTypeById(String id);

}

