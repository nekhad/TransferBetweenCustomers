package com.example.testforme.repository;

import com.example.testforme.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, String> {

    List<Currency> findByUpdatedDateBefore(LocalDateTime localDateTime);

}
