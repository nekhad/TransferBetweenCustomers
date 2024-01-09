package com.example.testforme.service;

import com.example.testforme.dto.*;
import com.example.testforme.entity.Accounts;
import com.example.testforme.exception.*;
import com.example.testforme.repository.AccountsRepository;
import com.example.testforme.repository.UserRepository;
import com.example.testforme.security.User;
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
        if (dto.getAccountNumber().length() == 16) {
            if (!repository.existsByAccountNumber(dto.getAccountNumber())) {
                if(dto.getCurrency().equals("AZN")){
                    Accounts accounts = Accounts.builder()
                            .accountNumber(dto.getAccountNumber())
                            .cvc(dto.getCvc())
                            .currency(dto.getCurrency())
                            .expirationDate(dto.getExpirationDate())
                            .isActive(dto.getIsActive())
                            .balance(0)
                            .status("A")
                            .currencyRate("1")
                            .user(userRepository.getUserByToken(dto.getToken()))
                            .build();
                    repository.save(accounts);
                }else if(dto.getCurrency().equals("USD")){
                    Accounts accounts = Accounts.builder()
                            .accountNumber(dto.getAccountNumber())
                            .cvc(dto.getCvc())
                            .currency(dto.getCurrency())
                            .expirationDate(dto.getExpirationDate())
                            .isActive(dto.getIsActive())
                            .balance(0)
                            .status("A")
                            .currencyRate("1.7")
                            .user(userRepository.getUserByToken(dto.getToken()))
                            .build();
                    repository.save(accounts);
                }
                else if(dto.getCurrency().equals("EURO")){
                    Accounts accounts = Accounts.builder()
                            .accountNumber(dto.getAccountNumber())
                            .cvc(dto.getCvc())
                            .currency(dto.getCurrency())
                            .expirationDate(dto.getExpirationDate())
                            .isActive(dto.getIsActive())
                            .balance(0)
                            .status("A")
                            .currencyRate("2")
                            .user(userRepository.getUserByToken(dto.getToken()))
                            .build();
                    repository.save(accounts);
                }
                else if(dto.getCurrency().equals("TL")){
                    Accounts accounts = Accounts.builder()
                            .accountNumber(dto.getAccountNumber())
                            .cvc(dto.getCvc())
                            .currency(dto.getCurrency())
                            .expirationDate(dto.getExpirationDate())
                            .isActive(dto.getIsActive())
                            .balance(0)
                            .status("A")
                            .currencyRate("0.2")
                            .user(userRepository.getUserByToken(dto.getToken()))
                            .build();
                    repository.save(accounts);
                }

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

        Accounts fromAccount = repository.getByAccountNumber(fromAccountNumber);
        Accounts toAccount = repository.getByAccountNumber(toAccountNumber);

        validateAccounts(fromAccount, toAccount);

        double amount = Double.parseDouble(dto.getAmount());
        String  currencyRateOfToAccountNumber = repository.getCurrencyRateByAccountNumber(toAccountNumber);
        String currencyRateOfFromAccountNumber = repository.getCurrencyRateByAccountNumber(fromAccountNumber);

        double currencyRateOfToAccountNumberDouble = Double.parseDouble(currencyRateOfToAccountNumber);
        double currencyRateOfFromAccountNumberDouble = Double.parseDouble(currencyRateOfFromAccountNumber);

        System.out.println(currencyRateOfToAccountNumberDouble);
        System.out.println(currencyRateOfFromAccountNumberDouble);
        validateAccountIsActive(toAccount);
        validateAccountIsActive(fromAccount);

        validateSufficientBalance(fromAccount, amount);

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        System.out.println((amount*currencyRateOfFromAccountNumberDouble)/currencyRateOfToAccountNumberDouble);
        toAccount.setBalance(toAccount.getBalance() + ((amount*currencyRateOfFromAccountNumberDouble)/currencyRateOfToAccountNumberDouble));

        repository.save(fromAccount);
        repository.save(toAccount);
    }

    private void validateAccounts(Accounts fromAccount, Accounts toAccount) {
        if (fromAccount.equals(toAccount)) {
            throw new SameAccountException();
        }
    }

    private void validateAccountIsActive(Accounts account) {
        if (!account.getIsActive().equals("active")) {
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
        System.out.println(accountsDto);
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
