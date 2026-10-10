package com.example.Finguard.domain.account.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountSummaryDto {
    private UUID accountId;
    private UUID userId;
    private String accountNumber;
    private BigDecimal balance;
    private String currency;
    private String status;

    public AccountSummaryDto(UUID accountId, UUID userId, String accountNumber, BigDecimal balance, String currency, String status) {
        this.accountId = accountId;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
        this.status = status;
    }

    public UUID getAccountId() { return accountId; }
    public UUID getUserId() { return userId; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }
    public String getStatus() { return status; }
}
