package com.example.testforme.service;

import com.example.testforme.dto.*;
import com.example.testforme.entity.Accounts;
import com.example.testforme.exception.*;
import com.example.testforme.repository.AccountsRepository;
import com.example.testforme.repository.CurrencyRepository;
import com.example.testforme.repository.UserRepository;
import com.example.testforme.security.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountsService {
    private final AccountsRepository repository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    public void create(AccountsCreateRequestDTO dto) {
        if (dto.getAccountNumber().length() == 16) {
            if (!repository.existsByAccountNumber(dto.getAccountNumber())) {
                Accounts accounts = Accounts.builder()
                        .accountNumber(dto.getAccountNumber())
                        .cvc(dto.getCvc())
                        .currency(currencyRepository.getCurrencyTypeById(dto.getCurrencyId()))
                        .expirationDate(dto.getExpirationDate())
                        .isActive(dto.getIsActive())
                        .balance(0)
                        .status("A")
                        .currencyRate(dto.getCurrencyId())
                        .user(userRepository.getUserByToken(dto.getToken()))
                        .build();
                repository.save(accounts);


            } else {
                throw new NotUniqueAccountNumber();
            }
        } else {
            throw new AccountNumberLengthException();
        }
    }

    public void transfer(AccountsTransferDTO dto) {
        String fromAccountNumber = dto.getFromAccountNumber();
        String toAccountNumber = dto.getToAccountNumber();
        String currencyId = dto.getCurrency();
        Accounts fromAccount = repository.getByAccountNumber(fromAccountNumber);
        Accounts toAccount = repository.getByAccountNumber(toAccountNumber);

        String currencyRate = currencyRepository.getCurrencyRateByCurrencyId(currencyId);
        double currencyRateDouble = Double.parseDouble(currencyRate);
        validateAccounts(fromAccount, toAccount);

        double amount = Double.parseDouble(dto.getAmount());
        String currencyRateOfToAccountNumber = repository.getCurrencyRateByAccountNumber(toAccountNumber);
        String currencyRateOfFromAccountNumber = repository.getCurrencyRateByAccountNumber(fromAccountNumber);

        double currencyRateOfToAccountNumberDouble = Double.parseDouble(currencyRateOfToAccountNumber);
        double currencyRateOfFromAccountNumberDouble = Double.parseDouble(currencyRateOfFromAccountNumber);

        log.info("currencyRateOfToAccountNumberDouble equals : " + currencyRateOfToAccountNumberDouble);
        log.info("currencyRateOfFromAccountNumberDouble equals : " + currencyRateOfFromAccountNumberDouble);
        validateAccountIsActive(toAccount);
        validateAccountIsActive(fromAccount);

        validateSufficientBalance(fromAccount, (amount * currencyRateDouble )/currencyRateOfFromAccountNumberDouble);

        fromAccount.setBalance(Math.round((fromAccount.getBalance() - ((amount * currencyRateDouble )/currencyRateOfFromAccountNumberDouble)) * 10 ) / 10.0);
        log.info( "Reduced amount equals : " + (Math.round((fromAccount.getBalance() - ((amount * currencyRateDouble )/currencyRateOfFromAccountNumberDouble)) * 10 ) / 10.0));
        toAccount.setBalance(Math.round((toAccount.getBalance() + ((amount * currencyRateDouble )/currencyRateOfToAccountNumberDouble)) * 10 ) / 10.0);
        log.info( "Increased amount equals : " + (Math.round((toAccount.getBalance() + ((amount * currencyRateDouble )/currencyRateOfToAccountNumberDouble)) * 10 ) / 10.0));

        repository.save(fromAccount);
        repository.save(toAccount);
    }

    private void validateAccounts(Accounts fromAccount, Accounts toAccount) {
        if (fromAccount.equals(toAccount)) {
            throw new SameAccountException();
        }
    }

    private void validateAccountIsActive(Accounts account) {
        if (!account.getIsActive().equals("A")) {
            throw new AccountNotActiveException();
        }
    }

    private void validateSufficientBalance(Accounts account, double amount) {
        if (account.getBalance() < amount) {
            throw new BalanceIsNotEnoughException();
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

    public List<AccountsDto> getActiveCardsOfUser(TokenRequest tokenRequest) {

        List<Accounts> accounts = repository.getActiveAccountsOfOwners(tokenRequest.getToken());
        List<AccountsDto> accountsDto = new ArrayList<>();
        User user = userRepository.getUserByToken(tokenRequest.getToken());
        for (Accounts account : accounts
        ) {
            AccountsDto accountsDto1 = AccountsDto.builder()
                    .id(account.getId())
                    .accountNumber(account.getAccountNumber())
                    .expirationDate(account.getExpirationDate())
                    .isActive(account.getIsActive())
                    .currency(account.getCurrency())
                    .cvc(account.getCvc())
                    .firstName(user.getFirstname())
                    .lastName(user.getLastname())
                    .build();
            accountsDto.add(accountsDto1);
        }
        return accountsDto;
    }

    public List<AccountsDto> getNonActiveCardsOfUser(TokenRequest tokenRequest) {

        List<Accounts> accounts = repository.getNonActiveAccountsOfOwners(tokenRequest.getToken());
        List<AccountsDto> accountsDto = new ArrayList<>();

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
            accountsDto.add(accountsDto1);
        }
        return accountsDto;
    }


}
