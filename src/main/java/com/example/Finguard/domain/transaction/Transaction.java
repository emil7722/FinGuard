package com.example.Finguard.domain.transaction;

import com.example.Finguard.domain.account.Account;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency = "USD";

    @Column(nullable = false)
    private String type; // TRANSFER, DEPOSIT, WITHDRAWAL

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, COMPLETED, FAILED, REJECTED

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public Transaction() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Account getSourceAccount() { return sourceAccount; }
    public void setSourceAccount(Account sourceAccount) { this.sourceAccount = sourceAccount; }
    public Account getDestinationAccount() { return destinationAccount; }
    public void setDestinationAccount(Account destinationAccount) { this.destinationAccount = destinationAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}