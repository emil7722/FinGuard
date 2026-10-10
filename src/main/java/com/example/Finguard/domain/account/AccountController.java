package com.example.Finguard.domain.account;

import com.example.Finguard.domain.account.dto.AccountResponseDto;
import com.example.Finguard.domain.account.dto.AccountSummaryDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountRequestDto request) {
        Account createdAccount = accountService.createAccount(request);
        AccountResponseDto response = new AccountResponseDto(
                createdAccount.getId(),
                createdAccount.getUser().getId(),
                createdAccount.getAccountNumber(),
                createdAccount.getBalance(),
                createdAccount.getCurrency(),
                createdAccount.getStatus()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts().stream()
                .map(account -> new AccountResponseDto(
                        account.getId(),
                        account.getUser().getId(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getCurrency(),
                        account.getStatus()))
                .toList());
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable UUID id) {
        Account account = accountService.getAccountByIdForCurrentUser(id);
        AccountResponseDto response = new AccountResponseDto(
                account.getId(),
                account.getUser().getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<AccountResponseDto>> getAccountsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserIdForCurrentUser(userId).stream()
                .map(account -> new AccountResponseDto(
                        account.getId(),
                        account.getUser().getId(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getCurrency(),
                        account.getStatus()))
                .toList());
    }

    @GetMapping("/{id}/summary")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<AccountSummaryDto> getAccountSummary(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccountSummary(id));
    }
}
