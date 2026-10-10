package com.example.Finguard.domain.transaction;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Transaction> transfer(@Valid @RequestBody TransferRequestDto request) {
        Transaction transaction = transactionService.processTransfer(request);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }
}
