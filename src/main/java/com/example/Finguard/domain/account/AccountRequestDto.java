package com.example.Finguard.domain.account;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountRequestDto {
    private UUID userId;
    private String accountNumber;
    private BigDecimal initialBalance;
    private String currency;

    // Getters and Setters
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public BigDecimal getInitialBalance() { return initialBalance; }
    public void setInitialBalance(BigDecimal initialBalance) { this.initialBalance = initialBalance; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}