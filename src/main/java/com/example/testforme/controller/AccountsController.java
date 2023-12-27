package com.example.testforme.controller;

import com.example.testforme.dto.*;
import com.example.testforme.entity.Accounts;
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

    @PostMapping("/add")
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
    @PostMapping("/getAll")
    public ResponseEntity<List<AccountsDto>> getAllAccounts(@RequestBody String  token) {

        return ResponseEntity.ok(service.getAll(token));
    }

//    @PostMapping("/getByToken")
//    public ResponseEntity<List<Accounts>> getAccountsByToken(@RequestBody String token) {
//        List<Accounts> accounts = service.getAccountsByToken(token);
//        return ResponseEntity.ok(accounts);
//    }


}
