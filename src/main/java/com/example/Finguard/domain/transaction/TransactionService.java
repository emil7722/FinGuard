package com.example.Finguard.domain.transaction;

import com.example.Finguard.domain.account.Account;
import com.example.Finguard.domain.account.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Transaction processTransfer(TransferRequestDto request) {
        // 1. Idempotency check: If this key was already processed, return the existing transaction
        if (request.getIdempotencyKey() != null) {
            var existing = transactionRepository.findByIdempotencyKey(request.getIdempotencyKey());
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency() != null ? request.getCurrency() : "USD");
        transaction.setType("TRANSFER");
        transaction.setIdempotencyKey(request.getIdempotencyKey());

        try {
            // 2. Fetch Source and Destination Accounts
            Account source = accountRepository.findById(request.getSourceAccountId())
                    .orElseThrow(() -> new RuntimeException("Source account not found"));

            Account destination = accountRepository.findById(request.getDestinationAccountId())
                    .orElseThrow(() -> new RuntimeException("Destination account not found"));

            transaction.setSourceAccount(source);
            transaction.setDestinationAccount(destination);

            // 3. Validate Sufficient Funds
            if (source.getBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Insufficient funds in source account");
            }

            // 4. Perform Transfer
            source.setBalance(source.getBalance().subtract(request.getAmount()));
            destination.setBalance(destination.getBalance().add(request.getAmount()));

            accountRepository.save(source);
            accountRepository.save(destination);

            // 5. Mark as Completed
            transaction.setStatus("COMPLETED");

        } catch (Exception e) {
            transaction.setStatus("FAILED");
            transaction.setFailureReason(e.getMessage());
            transactionRepository.save(transaction);
            throw e; // Ensures the transaction rolls back
        }

        return transactionRepository.save(transaction);
    }
}