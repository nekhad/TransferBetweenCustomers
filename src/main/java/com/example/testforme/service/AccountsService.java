package com.example.testforme.service;

import com.example.testforme.dto.AccountsCreateRequestDTO;
import com.example.testforme.dto.AccountsRequestDTO;
import com.example.testforme.entity.Accounts;
import com.example.testforme.exception.NotUniqueAccountNumber;
import com.example.testforme.repository.AccountsRepository;
import com.example.testforme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountsService {
    private final AccountsRepository repository;
    private final UserRepository userRepository;

    public void create(AccountsCreateRequestDTO dto) {
//        if (!repository.existsByAccountNumber(dto.getAccountNumber())) {
            Accounts accounts = Accounts.builder()
                    .accountNumber(dto.getAccountNumber())
                    .cvc(dto.getCvc())
                    .currency(dto.getCurrency())
                    .expirationDate(dto.getExpirationDate())
                    .isActive(dto.isActive())
                    .balance(0)
                    .status("A").build();
//                    .user(userRepository.getReferenceById(dto.getUserId())).build();
            repository.save(accounts);
//        } else {
//            throw new NotUniqueAccountNumber();
//        }
    }

    public void update(AccountsRequestDTO dto) {
        Accounts accounts = repository.getById(dto.getId());
        accounts.setAccountNumber(dto.getAccountNumber());
        accounts.setCvc(dto.getCvc());
        accounts.setCurrency(dto.getCurrency());
        accounts.setExpirationDate(dto.getExpirationDate());
        accounts.setActive(dto.isActive());
        repository.save(accounts);
    }

    public void delete(String id) {
        Accounts accounts = repository.getById(id);
        accounts.setStatus("D");
        repository.save(accounts);
    }

    public List<Accounts> getAll(String userId) {
        List<Accounts> accounts = repository.getAccountsByUserId(userId);
        List<Accounts> accountsList = new ArrayList<>();
        for (Accounts account : accounts) {
            if (account.getStatus().equals("A")) {
                accountsList.add(account);
            }
        }
        return accountsList;
    }


}
