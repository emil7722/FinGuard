package com.example.Finguard.domain.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequestDto request) {
        Account createdAccount = accountService.createAccount(request);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    // NEW: Get all accounts in the system
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // NEW: Get single account by ID
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }
}