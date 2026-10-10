package com.example.Finguard.domain.transaction;

import com.example.Finguard.common.exception.InsufficientFundsException;
import com.example.Finguard.common.exception.NotFoundException;
import com.example.Finguard.common.exception.ValidationException;
import com.example.Finguard.domain.account.Account;
import com.example.Finguard.domain.account.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        if (request.getSourceAccountId().equals(request.getDestinationAccountId())) {
            throw new ValidationException("Source and destination accounts cannot be the same");
        }

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
            Account source = accountRepository.findById(request.getSourceAccountId())
                    .orElseThrow(() -> new NotFoundException("Account", request.getSourceAccountId().toString()));

            Account destination = accountRepository.findById(request.getDestinationAccountId())
                    .orElseThrow(() -> new NotFoundException("Account", request.getDestinationAccountId().toString()));

            if (!"ACTIVE".equals(source.getStatus())) {
                throw new ValidationException("Source account is not active");
            }
            if (!"ACTIVE".equals(destination.getStatus())) {
                throw new ValidationException("Destination account is not active");
            }

            transaction.setSourceAccount(source);
            transaction.setDestinationAccount(destination);

            if (source.getBalance().compareTo(request.getAmount()) < 0) {
                throw new InsufficientFundsException(
                        "Insufficient funds. Available: " + source.getBalance() + ", Requested: " + request.getAmount()
                );
            }

            source.setBalance(source.getBalance().subtract(request.getAmount()));
            destination.setBalance(destination.getBalance().add(request.getAmount()));

            accountRepository.save(source);
            accountRepository.save(destination);
            transaction.setStatus("COMPLETED");

        } catch (Exception e) {
            transaction.setStatus("FAILED");
            transaction.setFailureReason(e.getMessage());
            transactionRepository.save(transaction);
            throw e;
        }

        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByAccount(java.util.UUID accountId) {
        return transactionRepository.findAll().stream()
                .filter(transaction ->
                        (transaction.getSourceAccount() != null && transaction.getSourceAccount().getId().equals(accountId)) ||
                        (transaction.getDestinationAccount() != null && transaction.getDestinationAccount().getId().equals(accountId)))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }
}
