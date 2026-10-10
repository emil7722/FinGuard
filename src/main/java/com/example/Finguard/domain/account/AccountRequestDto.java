package com.example.Finguard.domain.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public class AccountRequestDto {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "Initial balance is required")
    @Positive(message = "Initial balance must be positive")
    private BigDecimal initialBalance;

    private String currency = "USD";

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