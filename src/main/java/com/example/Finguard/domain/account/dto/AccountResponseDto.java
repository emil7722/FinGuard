package com.example.Finguard.domain.account.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountResponseDto {
    private UUID id;
    private UUID userId;
    private String accountNumber;
    private BigDecimal balance;
    private String currency;
    private String status;

    public AccountResponseDto(UUID id, UUID userId, String accountNumber, BigDecimal balance, String currency, String status) {
        this.id = id;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
        this.status = status;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }
    public String getStatus() { return status; }
}
