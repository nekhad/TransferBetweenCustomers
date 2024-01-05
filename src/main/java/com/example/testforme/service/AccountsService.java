package com.example.testforme.service;

import com.example.testforme.dto.AccountsCreateRequestDTO;
import com.example.testforme.dto.AccountsDto;
import com.example.testforme.dto.AccountsRequestDTO;
import com.example.testforme.entity.Accounts;
import com.example.testforme.exception.NotUniqueAccountNumber;
import com.example.testforme.repository.AccountsRepository;
import com.example.testforme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountsService {
    private final AccountsRepository repository;
    private final UserRepository userRepository;

    public void create(AccountsCreateRequestDTO dto) {
        if (!repository.existsByAccountNumber(dto.getAccountNumber())) {
            Accounts accounts = Accounts.builder()
                    .accountNumber(dto.getAccountNumber())
                    .cvc(dto.getCvc())
                    .currency(dto.getCurrency())
                    .expirationDate(dto.getExpirationDate())
                    .isActive(dto.getIsActive())
                    .balance(0)
                    .status("A")
                    .user(userRepository.getUserByToken(dto.getToken()))
                    .build();
            repository.save(accounts);
        } else {
            throw new NotUniqueAccountNumber();
        }
    }

    public void update(AccountsRequestDTO dto) {
        Accounts accounts = repository.getById(dto.getId());
        accounts.setAccountNumber(dto.getAccountNumber());
        accounts.setCvc(dto.getCvc());
        accounts.setCurrency(dto.getCurrency());
        accounts.setExpirationDate(dto.getExpirationDate());
        accounts.setIsActive(dto.getIsActive());
        repository.save(accounts);
    }

    public void delete(String id) {
        Accounts accounts = repository.getById(id);
        accounts.setStatus("D");
        repository.save(accounts);
    }
    public List<AccountsDto> getAll(String token) {
        List<Accounts> accounts = repository.getAccountsOfUser(token);
        System.out.println(token);
        List<AccountsDto> accountsDtos = new ArrayList<>();

        for (Accounts account : accounts
        ) {
            AccountsDto accountsDto1 = AccountsDto.builder()
                    .id(account.getId())
                    .accountNumber(account.getAccountNumber())
                    .expirationDate(account.getExpirationDate())
                    .isActive(account.getIsActive())
                    .currency(account.getCurrency())
                    .cvc(account.getCvc())
                    .build();
            accountsDtos.add(accountsDto1);

        }
        return accountsDtos;
    }

//    public List<Accounts> getAccountsByToken(String token) {
//        return repository.getAccountsByToken(token);
//    }


}
