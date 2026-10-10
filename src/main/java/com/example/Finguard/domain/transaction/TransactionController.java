package com.example.Finguard.domain.transaction;

import com.example.Finguard.domain.transaction.dto.TransactionResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<TransactionResponseDto> transfer(@Valid @RequestBody TransferRequestDto request) {
        Transaction transaction = transactionService.processTransfer(request);
        TransactionResponseDto response = new TransactionResponseDto(
                transaction.getId(),
                transaction.getSourceAccount() != null ? transaction.getSourceAccount().getId() : null,
                transaction.getDestinationAccount() != null ? transaction.getDestinationAccount().getId() : null,
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getFailureReason(),
                transaction.getIdempotencyKey()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/account/{accountId}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionHistory(
            @PathVariable UUID accountId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Transaction> transactions = transactionService.getTransactionsByAccount(accountId, status, page, size);

        Page<TransactionResponseDto> response = transactions.map(transaction -> new TransactionResponseDto(
                transaction.getId(),
                transaction.getSourceAccount() != null ? transaction.getSourceAccount().getId() : null,
                transaction.getDestinationAccount() != null ? transaction.getDestinationAccount().getId() : null,
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getFailureReason(),
                transaction.getIdempotencyKey()
        ));

        return ResponseEntity.ok(response);
    }
}
