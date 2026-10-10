package com.example.Finguard.domain.transaction.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionResponseDto {
    private UUID id;
    private UUID sourceAccountId;
    private UUID destinationAccountId;
    private BigDecimal amount;
    private String currency;
    private String type;
    private String status;
    private String failureReason;
    private String idempotencyKey;

    public TransactionResponseDto(UUID id, UUID sourceAccountId, UUID destinationAccountId, BigDecimal amount,
                                 String currency, String type, String status, String failureReason, String idempotencyKey) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.status = status;
        this.failureReason = failureReason;
        this.idempotencyKey = idempotencyKey;
    }

    public UUID getId() { return id; }
    public UUID getSourceAccountId() { return sourceAccountId; }
    public UUID getDestinationAccountId() { return destinationAccountId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getFailureReason() { return failureReason; }
    public String getIdempotencyKey() { return idempotencyKey; }
}
