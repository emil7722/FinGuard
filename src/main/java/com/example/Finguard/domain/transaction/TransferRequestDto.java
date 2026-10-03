package com.example.Finguard.domain.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferRequestDto {
    private UUID sourceAccountId;
    private UUID destinationAccountId;
    private BigDecimal amount;
    private String currency;
    private String idempotencyKey;

    // Getters and Setters
    public UUID getSourceAccountId() { return sourceAccountId; }
    public void setSourceAccountId(UUID sourceAccountId) { this.sourceAccountId = sourceAccountId; }
    public UUID getDestinationAccountId() { return destinationAccountId; }
    public void setDestinationAccountId(UUID destinationAccountId) { this.destinationAccountId = destinationAccountId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
}