package com.example.testforme.controller;

import com.example.testforme.dto.AccountsCreateRequestDTO;
import com.example.testforme.dto.AccountsDeleteDTO;
import com.example.testforme.dto.AccountsRequestDTO;
import com.example.testforme.dto.AccountsResponseDTO;
import com.example.testforme.service.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<AccountsResponseDTO> getAllAccounts(@RequestBody String  userId) {
        service.getAll(userId);
        return ResponseEntity.ok(new AccountsResponseDTO("Accounts were received successfully"));
    }


}
