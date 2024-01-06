package com.example.testforme.controller;

import com.example.testforme.dto.*;
import com.example.testforme.service.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/accounts")
@RequiredArgsConstructor
public class AccountsController {
    private final AccountsService service;

    @PostMapping("/create")
    public ResponseEntity<AccountsResponseDTO> addAccount(@RequestBody AccountsCreateRequestDTO requestDTO) {
        service.create(requestDTO);
        return ResponseEntity.ok(new AccountsResponseDTO("Account was created"));
    }

    @PostMapping("/delete")
    public ResponseEntity<AccountsResponseDTO> deleteAccount(@RequestBody AccountsDeleteDTO requestDTO) {
        service.delete(requestDTO.getId());
        return ResponseEntity.ok(new AccountsResponseDTO("Account was deleted successfully"));
    }

    @PostMapping("/update")
    public ResponseEntity<AccountsResponseDTO> updateAccount(@RequestBody AccountsRequestDTO requestDTO) {
        service.update(requestDTO);
        return ResponseEntity.ok(new AccountsResponseDTO("Account was updated successfully"));
    }

    @PostMapping("/get-active-cards")
    public ResponseEntity<List<AccountsDto>> getActiveAccountsOfUser(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(service.getActiveCardsOfUser(tokenRequest));
    }
    @PostMapping("/get-non-active-cards")
    public ResponseEntity<List<AccountsDto>> getNonActiveAccountsOfUser(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(service.getNonActiveCardsOfUser(tokenRequest));
    }

    @PostMapping("/transfer")
    public ResponseEntity<AccountsResponseDTO> transfer(@RequestBody AccountsTransferDTO dto) {
        service.transfer(dto);
        return ResponseEntity.ok(new AccountsResponseDTO("Transfer was successful"));
    }


}
